package com.yinliang.Lucene_3_5.custom_score;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.function.CustomScoreProvider;
import org.apache.lucene.search.function.CustomScoreQuery;
import org.apache.lucene.search.function.FieldScoreQuery;
import org.apache.lucene.search.function.FieldScoreQuery.Type;
import org.apache.lucene.search.function.ValueSourceQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class MyScoreQuery {
	private static Directory directory = null;
	private static IndexReader indexReader = null;
	private static final ReentrantLock reentrantLock = new ReentrantLock();

	public MyScoreQuery(String path) {
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

	public void searchByCustomScore() {
		IndexSearcher indexSearcher = getIndexSearcher();
		try {
			Query query = new TermQuery(new Term("description", "dentist"));

			// 1.创建一个评分
			FieldScoreQuery fieldScoreQuery = new FieldScoreQuery("length", Type.INT);

			// 2.根据评分域和原有的Query创建自定义的Query对象
			MyCustomScoreQuery myCustomScoreQuery = new MyCustomScoreQuery(query, fieldScoreQuery);

			TopDocs tds = indexSearcher.search(myCustomScoreQuery, 1000);

			for (ScoreDoc sd : tds.scoreDocs) {
				Document document = indexSearcher.doc(sd.doc);
				System.out.println(String.format("ID:%4d, score:%10s, film_id:%10s, title:%30s, length:%10s, date:%15s", sd.doc, sd.score, document.get("film_id"), document.get("title"), document.get("length"), document.get("date")));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (indexSearcher != null) {
				try {
					indexSearcher.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void searchByCustomScore_Title() {
		IndexSearcher indexSearcher = getIndexSearcher();
		try {
			Query query = new TermQuery(new Term("description", "dentist"));

			// 2.根据评分域和原有的Query创建自定义的Query对象
			MyCustomScoreQuery_Title myCustomScoreQuery = new MyCustomScoreQuery_Title(query);

			TopDocs tds = indexSearcher.search(myCustomScoreQuery, Integer.MAX_VALUE);

			for (ScoreDoc sd : tds.scoreDocs) {
				Document document = indexSearcher.doc(sd.doc);
				System.out.println(String.format("ID:%4d, score:%10s, film_id:%10s, title:%30s, length:%10s, date:%15s", sd.doc, sd.score, document.get("film_id"), document.get("title"), document.get("length"), document.get("date")));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (indexSearcher != null) {
				try {
					indexSearcher.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void searchByCustomScore_Length() {
		IndexSearcher indexSearcher = getIndexSearcher();
		try {
			Query query = new TermQuery(new Term("description", "dentist"));

			// 2.根据评分域和原有的Query创建自定义的Query对象
			MyCustomScoreQuery_Length myCustomScoreQuery = new MyCustomScoreQuery_Length(query);

			TopDocs tds = indexSearcher.search(myCustomScoreQuery, Integer.MAX_VALUE);

			for (ScoreDoc sd : tds.scoreDocs) {
				Document document = indexSearcher.doc(sd.doc);
				System.out.println(String.format("ID:%4d, score:%10s, film_id:%10s, title:%30s, length:%10s, date:%15s", sd.doc, sd.score, document.get("film_id"), document.get("title"), document.get("length"), document.get("date")));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (indexSearcher != null) {
				try {
					indexSearcher.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

class MyCustomScoreQuery extends CustomScoreQuery {

	// 第一个参数是原有的query,第二个参数时评分的query
	public MyCustomScoreQuery(Query subQuery, ValueSourceQuery valSrcQuery) {
		super(subQuery, valSrcQuery);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected CustomScoreProvider getCustomScoreProvider(IndexReader reader) throws IOException {

		// 默认的评分是通过原有的评分乘于传入的评分的域所获得的评分来确定最终评分
		// return super.getCustomScoreProvider(reader);

		// 为了根据不同的需求来进行评分,需要进行评分规则的设定,设定步骤如下
		/*
		 * 1.创建一个类继承于CustomScoreProvider
		 * 2.覆盖customScore方法
		 */
		return new MyCustomScoreProvider(reader);
	}

	class MyCustomScoreProvider extends CustomScoreProvider {

		public MyCustomScoreProvider(IndexReader reader) {
			super(reader);
			// TODO Auto-generated constructor stub
		}

		/*
		 * subQueryScore表示默认文档的评分
		 * valSrcScores表示评分域的评分
		 */
		@Override
		public float customScore(int doc, float subQueryScore, float[] valSrcScores) throws IOException {
			// TODO Auto-generated method stub

			return subQueryScore / valSrcScores[0];
			// return super.customScore(doc, subQueryScore, valSrcScores);
		}

	}
}

class MyCustomScoreQuery_Title extends CustomScoreQuery {

	public MyCustomScoreQuery_Title(Query subQuery) {
		super(subQuery);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected CustomScoreProvider getCustomScoreProvider(IndexReader reader) throws IOException {
		// TODO Auto-generated method stub
		return new MyCustomScoreProvider_Title(reader);
	}

	class MyCustomScoreProvider_Title extends CustomScoreProvider {
		String titles[] = null;

		public MyCustomScoreProvider_Title(IndexReader reader) {
			super(reader);

			/*
			 * 在reader 没有关闭之前, 所有的数据会保存在一个域缓存中, 可以通过域缓存获取更多有用的信息
			 */
			try {
				this.titles = FieldCache.DEFAULT.getStrings(reader, "title");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		public float customScore(int doc, float subQueryScore, float valSrcScore) throws IOException {
			// 根据传入的参数 doc 获取相应的document的field

			if (this.titles != null) {
				if (this.titles[doc].toLowerCase().startsWith("z")) {
					System.out.println(doc);
					return subQueryScore * 1000f;
				}
			}
			return super.customScore(doc, subQueryScore, valSrcScore) / 1.5f;
		}

	}
}

class MyCustomScoreQuery_Length extends CustomScoreQuery {

	public MyCustomScoreQuery_Length(Query subQuery) {
		super(subQuery);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected CustomScoreProvider getCustomScoreProvider(IndexReader reader) throws IOException {
		// TODO Auto-generated method stub
		return new MyCustomScoreProvider_Length(reader);
	}

	class MyCustomScoreProvider_Length extends CustomScoreProvider {
		int[] lengths = null;

		public MyCustomScoreProvider_Length(IndexReader reader) {
			super(reader);

			/*
			 * 在reader 没有关闭之前, 所有的数据会保存在一个域缓存中, 可以通过域缓存获取更多有用的信息
			 */
			try {
				this.lengths = FieldCache.DEFAULT.getInts(reader, "length");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		public float customScore(int doc, float subQueryScore, float valSrcScore) throws IOException {
			// 根据传入的参数 doc 获取相应的document的field

			if (this.lengths != null) {
				if ((this.lengths[doc] % 10) == 0) {
					System.out.println(doc);
					return subQueryScore * 1.5f;
				}
			}
			return super.customScore(doc, subQueryScore, valSrcScore) / 1.5f;
		}
	}
}
