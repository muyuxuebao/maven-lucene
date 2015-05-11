package com.yinliang.Lucene_3_5.highlighter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;

public class HighlighterUtil {

	String filePath = "multipleFile";
	String indexPath = "multipleFileIndex";

	private static Directory directory = null;
	private static IndexReader indexReader = null;
	private static final ReentrantLock reentrantLock = new ReentrantLock();

	public HighlighterUtil() {
		try {
			directory = FSDirectory.open(new File(this.indexPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static IndexSearcher getIndexSearcher() {
		try {
			reentrantLock.lock();
			if (indexReader == null) {
				indexReader = IndexReader.open(directory);
			} else {
				IndexReader tr = IndexReader.openIfChanged(indexReader);
				if (tr != null) {
					indexReader.close();
					indexReader = tr;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			reentrantLock.unlock();
		}
		return new IndexSearcher(indexReader);
	}

	public void createIndex() {
		IndexWriter indexWriter = null;
		try {
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35, new MMSegAnalyzer());
			indexWriter = new IndexWriter(this.directory, iwc);
			Document doc = null;
			File files = new File(this.filePath);
			for (File file : files.listFiles()) {
				doc = this.fileToDocument(file);
				indexWriter.addDocument(doc);
			}

		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (indexWriter != null) {
				try {
					indexWriter.close();
				} catch (CorruptIndexException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private Document fileToDocument(File file) throws IOException {

		Document doc;
		doc = new Document();

		Metadata metadata = new Metadata();

		doc.add(new Field("content", new Tika().parse(new FileInputStream(file), metadata)));
		doc.add(new Field("title", FilenameUtils.getBaseName(file.getName()), Field.Store.YES, Field.Index.ANALYZED));
		doc.add(new Field("fileName", file.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("path", file.getAbsolutePath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("type", FilenameUtils.getExtension(file.getName()), Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new NumericField("date", Field.Store.YES, true).setLongValue(file.lastModified()));
		doc.add(new NumericField("size", Field.Store.YES, true).setLongValue(file.length()));

		int pageCount = 0;
		try {
			pageCount = Integer.parseInt(metadata.get("xmpTPg:NPages"));
		} catch (NumberFormatException e) {
		}
		doc.add(new NumericField("pageCount", Field.Store.YES, true).setLongValue(pageCount));
		return doc;
	}

	public void simpleDemo() {
		String txt = "毛泽东（1893年12月26日－1976年9月9日），字润之（原作咏芝，后改润芝），笔名子任。" + "湖南湘潭人。"
				+ "诗人，伟大的马克思主义者，无产阶级革命家、战略家和理论家，中国共产党、中国人民解放军和中华人民共和国的主要缔造者和领导人。" + "1949至1976年，毛泽东担任中华人民共和国最高领导人。"
				+ "他对马克思列宁主义的发展、军事理论的贡献以及对共产党的理论贡献被称为毛泽东思想。" + "因毛泽东担任过的主要职务几乎全部称为主席，所以也被人们尊称为“毛主席”。"
				+ "毛泽东被视为现代世界历史中最重要的人物之一，《时代》杂志也将他评为20世纪最具影响100人之一。";

		try {
			Query query = new QueryParser(Version.LUCENE_35, "f", new MMSegAnalyzer()).parse("湖南");
			QueryScorer scorer = new QueryScorer(query);
			Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);

			Formatter formatter = new SimpleHTMLFormatter("<span style = 'cokor:red'>", "</span>");

			Highlighter highlighter = new Highlighter(formatter, scorer);
			highlighter.setTextFragmenter(fragmenter);
			String str = highlighter.getBestFragment(new MMSegAnalyzer(), "f", txt);
			System.out.println(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void highlighterSearch(String fieldName, String value, int num) {
		IndexSearcher indexSearcher = null;

		if (fieldName.equals("content")) {

		} else if (fieldName.equals("title")) {
			try {
				indexSearcher = getIndexSearcher();
				QueryParser parser = new MultiFieldQueryParser(Version.LUCENE_35, new String[] { "title", "content" },
						new MMSegAnalyzer());
				Query query = parser.parse(value);

				TopDocs tds = indexSearcher.search(query, num);

				for (ScoreDoc sd : tds.scoreDocs) {
					Document doc = indexSearcher.doc(sd.doc);
					String title = this.getHighlightString(doc.get("title"), query);
					String fileName = doc.get("fileName");

					String content = this
							.getHighlightString(new Tika().parseToString(new File(doc.get("path"))), query);

					System.out.println("title:" + (title != null ? title : doc.get("title")));
					System.out.println("fileName: " + fileName);
					System.out.println("content:\n" + content);
					System.out.println("***********************************************************************");
				}
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TikaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private String getHighlightString(String value, Query query) {
		String str = null;
		try {
			QueryScorer scorer = new QueryScorer(query);
			Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
			Formatter formatter = new SimpleHTMLFormatter("<b>", "</b>");
			Highlighter highlighter = new Highlighter(formatter, scorer);
			highlighter.setTextFragmenter(fragmenter);

			str = highlighter.getBestFragment(new MMSegAnalyzer(), "title", value);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

}
