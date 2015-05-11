package com.yinliang.Lucene_3_5.test;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import com.yinliang.Lucene_3_5.analyzer.AnalyzerUtil;
import com.yinliang.Lucene_3_5.analyzer.synonym.MySynonymAnalyzer;

public class MySynonymAnalyzerTest {

	@Test
	public void test() {
		try {
			AnalyzerUtil analyzerUtil = new AnalyzerUtil();
			String txt = "这是我的家, 我来自中国安徽省六安市苍墩村";

			Directory directory = new RAMDirectory();

			Document doc = new Document();
			doc.add(new Field("content", txt, Field.Store.YES, Field.Index.ANALYZED));

			IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new MySynonymAnalyzer()));
			indexWriter.addDocument(doc);
			indexWriter.close();

			IndexSearcher indexSearcher = new IndexSearcher(IndexReader.open(directory));

			// 因为同义词的关系, 以下都可以搜到 content 为 "这是我的家, 我来自中国安徽省六安市苍墩村"的 Document
			TopDocs tds = indexSearcher.search(new TermQuery(new Term("content", "我")), 10);
			TopDocs tds1 = indexSearcher.search(new TermQuery(new Term("content", "咱")), 10);
			TopDocs tds2 = indexSearcher.search(new TermQuery(new Term("content", "俺")), 10);
			TopDocs tds3 = indexSearcher.search(new TermQuery(new Term("content", "中国")), 10);
			TopDocs tds4 = indexSearcher.search(new TermQuery(new Term("content", "天朝")), 10);
			TopDocs tds5 = indexSearcher.search(new TermQuery(new Term("content", "中华人民共和国")), 10);

			Document d = indexSearcher.doc(tds5.scoreDocs[0].doc);
			System.out.println(d.get("content"));

			// analyzerUtil.dispalyAllTokenInfo(txt, new MySynonymAnalyzer());
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
