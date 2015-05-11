package com.yinliang.Lucene_3_5.tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/*
 * Data come form the carried database of MySQL, it name is sakila.film
 */

public class TikaIndexUtil {
	private static Directory directory = null;
	private static IndexReader indexReader = null;
	private static final ReentrantLock reentrantLock = new ReentrantLock();

	public TikaIndexUtil(String path) {
		try {
			directory = FSDirectory.open(new File(path));
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

	public void searchIndex(String description) {
		IndexSearcher indexSearcher = getIndexSearcher();
		try {

			// 4.创建Parser来确定要搜索的文件的内容,第二个蚕食表示搜索的域
			QueryParser queryParser = new QueryParser(Version.LUCENE_35, "description", new StandardAnalyzer(Version.LUCENE_35));
			Query query = queryParser.parse(description);
			TopDocs tds = indexSearcher.search(query, 100);
			ScoreDoc[] sds = tds.scoreDocs;
			System.out.println(sds.length);
			for (ScoreDoc scoreDoc : sds) {
				Document document = indexSearcher.doc(scoreDoc.doc);
				System.out.println(String.format("ID:%4d, score:%10s, film_id:%10s, title:%30s, length:%10s, date:%15s", scoreDoc.doc, scoreDoc.score, document.get("film_id"), document.get("title"), document.get("length"), new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(document.get("date"))))));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (indexReader != null) {
				try {
					indexSearcher.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void createIndex() {

	}

	public String convertFileToString(File file) {
		Parser parser = new AutoDetectParser();
		InputStream is = null;

		try {
			is = new FileInputStream(file);

			// 获取的 String 保存在 ContentHandler 中
			ContentHandler handler = new BodyContentHandler();

			// 文件的各种信息都保存在 Metadata 中
			Metadata metadata = new Metadata();

			// 指定Parser
			ParseContext context = new ParseContext();
			context.set(Parser.class, parser);

			parser.parse(is, handler, metadata, context);

			// 输出文件的各种信息
			String[] names = metadata.names();
			for (String name : names) {
				System.out.println(name + ": " + metadata.get(name));
			}

			return handler.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (TikaException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public String convertFileToString2(File file) {
		Tika tika = new Tika();
		Metadata metadata = new Metadata();
		try {
			String str = tika.parseToString(new FileInputStream(file), metadata);

			// 输出文件的各种信息
			String[] names = metadata.names();
			for (String name : names) {
				System.out.println(name + ": " + metadata.get(name));
			}

			return str;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TikaException e) {
			e.printStackTrace();
		}

		return null;
	}
}
