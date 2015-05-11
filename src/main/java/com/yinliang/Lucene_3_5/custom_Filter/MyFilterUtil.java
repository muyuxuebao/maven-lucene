package com.yinliang.Lucene_3_5.custom_Filter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class MyFilterUtil {

	private static Directory directory = null;
	private static IndexReader indexReader = null;
	private static final ReentrantLock reentrantLock = new ReentrantLock();

	public MyFilterUtil(String path) {
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

	public void search(String field, String value, int num) {
		IndexSearcher indexSearcher = null;
		try {

			indexSearcher = MyFilterUtil.getIndexSearcher();
			Query query = new TermQuery(new Term(field, value));
			TopDocs topDocs = indexSearcher.search(query, new TitleFilter(), num);
			ScoreDoc[] sds = topDocs.scoreDocs;

			System.out.println(sds.length);

			System.out.println(sds.length);
			for (ScoreDoc scoreDoc : sds) {
				Document document = indexSearcher.doc(scoreDoc.doc);
				System.out.println(String.format("ID:%4d, score:%10s, film_id:%10s, title:%30s, length:%10s, date:%15s", scoreDoc.doc, scoreDoc.score, document.get("film_id"), document.get("title"), document.get("length"), new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(document.get("date"))))));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (indexSearcher != null) {
				try {
					indexSearcher.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
