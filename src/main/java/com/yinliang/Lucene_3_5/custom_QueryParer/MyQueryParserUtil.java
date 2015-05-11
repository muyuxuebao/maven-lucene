package com.yinliang.Lucene_3_5.custom_QueryParer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class MyQueryParserUtil {
	private static Directory directory = null;
	private static IndexReader indexReader = null;
	private static final ReentrantLock reentrantLock = new ReentrantLock();

	public MyQueryParserUtil(String path) {
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

	public void searchWildcard(int num) {
		IndexSearcher indexSearcher = null;
		try {
			indexSearcher = MyQueryParserUtil.getIndexSearcher();

			// 默认搜索description
			MyQueryParser parser = new MyQueryParser(Version.LUCENE_35, "description", new StandardAnalyzer(Version.LUCENE_35));
			parser.setDefaultOperator(Operator.AND);

			// 示例:模糊查询
			Query query8 = parser.parse("title:earth~");

			TopDocs topDocs = indexSearcher.search(query8, num);
			System.out.println("一共查询到了 :" + topDocs.totalHits);
			ScoreDoc[] sds = topDocs.scoreDocs;

			System.out.println(sds.length);
			for (ScoreDoc scoreDoc : sds) {
				Document document = indexSearcher.doc(scoreDoc.doc);
				System.out.println(String.format("ID:%4d, score:%10s, film_id:%10s, title:%30s, length:%10s, date:%15s", scoreDoc.doc, scoreDoc.score, document.get("film_id"), document.get("title"), document.get("length"), new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(document.get("date"))))));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
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

	public void searchFuzzy(int num) {
		IndexSearcher indexSearcher = null;
		try {
			indexSearcher = MyQueryParserUtil.getIndexSearcher();

			// 默认搜索description
			MyQueryParser parser = new MyQueryParser(Version.LUCENE_35, "description", new StandardAnalyzer(Version.LUCENE_35));
			parser.setDefaultOperator(Operator.AND);

			// 示例:模糊查询
			Query query8 = parser.parse("title:earth~");

			TopDocs topDocs = indexSearcher.search(query8, num);
			System.out.println("一共查询到了 :" + topDocs.totalHits);
			ScoreDoc[] sds = topDocs.scoreDocs;

			System.out.println(sds.length);
			for (ScoreDoc scoreDoc : sds) {
				Document document = indexSearcher.doc(scoreDoc.doc);
				System.out.println(String.format("ID:%4d, score:%10s, film_id:%10s, title:%30s, length:%10s, date:%15s", scoreDoc.doc, scoreDoc.score, document.get("film_id"), document.get("title"), document.get("length"), new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(document.get("date"))))));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
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

	public void searchRange_Length(int num) {
		IndexSearcher indexSearcher = null;
		try {
			indexSearcher = MyQueryParserUtil.getIndexSearcher();

			// 默认搜索description
			MyQueryParser parser = new MyQueryParser(Version.LUCENE_35, "description", new StandardAnalyzer(Version.LUCENE_35));
			parser.setDefaultOperator(Operator.AND);

			Query query8 = parser.parse("length:[80 TO 100]");

			TopDocs topDocs = indexSearcher.search(query8, num);
			System.out.println("一共查询到了 :" + topDocs.totalHits);
			ScoreDoc[] sds = topDocs.scoreDocs;

			System.out.println(sds.length);
			for (ScoreDoc scoreDoc : sds) {
				Document document = indexSearcher.doc(scoreDoc.doc);
				System.out.println(String.format("ID:%4d, score:%10s, film_id:%10s, title:%30s, length:%10s, date:%15s", scoreDoc.doc, scoreDoc.score, document.get("film_id"), document.get("title"), document.get("length"), new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(document.get("date"))))));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
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

	public void searchRange_Date(int num) {
		IndexSearcher indexSearcher = null;
		try {
			indexSearcher = MyQueryParserUtil.getIndexSearcher();

			// 默认搜索description
			MyQueryParser parser = new MyQueryParser(Version.LUCENE_35, "description", new StandardAnalyzer(Version.LUCENE_35));
			parser.setDefaultOperator(Operator.AND);

			Query query8 = parser.parse("date:[2000-01-01 TO 2020-01-01]");

			TopDocs topDocs = indexSearcher.search(query8, num);
			System.out.println("一共查询到了 :" + topDocs.totalHits);
			ScoreDoc[] sds = topDocs.scoreDocs;

			System.out.println(sds.length);
			for (ScoreDoc scoreDoc : sds) {
				Document document = indexSearcher.doc(scoreDoc.doc);
				System.out.println(String.format("ID:%4d, score:%10s, film_id:%10s, title:%30s, length:%10s, date:%15s", scoreDoc.doc, scoreDoc.score, document.get("film_id"), document.get("title"), document.get("length"), new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(document.get("date"))))));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
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
