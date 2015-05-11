package com.yinliang.Lucene_3_5.searcher;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class SearchUtil {
	private static Directory directory = null;
	private static IndexReader indexReader = null;
	private static final ReentrantLock reentrantLock = new ReentrantLock();

	public SearchUtil(String path) {
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

	// 精确的查找
	public void searchByTerm(String field, String value, int num) {
		IndexSearcher indexSearcher = null;
		try {
			indexSearcher = SearchUtil.getIndexSearcher();
			Query query = new TermQuery(new Term(field, value));
			TopDocs topDocs = indexSearcher.search(query, num);
			ScoreDoc[] sds = topDocs.scoreDocs;

			System.out.println(sds.length);
			for (ScoreDoc scoreDoc : sds) {
				Document document = indexSearcher.doc(scoreDoc.doc);
				System.out.println(String.format(
						"ID:%4d, score:%10s, film_id:%10s, title:%30s, length:%10s, date:%15s", scoreDoc.doc,
						scoreDoc.score, document.get("film_id"), document.get("title"), document.get("length"),
						new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(document.get("date"))))));
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

	// 范围查找
	public void searchByRangeTerm(String field, String lValue, String hValue, int num) {
		IndexSearcher indexSearcher = null;
		try {
			indexSearcher = SearchUtil.getIndexSearcher();

			// 第四五个参数表示是否包括上下边界的值
			Query query = new TermRangeQuery(field, lValue, hValue, true, true);

			TopDocs topDocs = indexSearcher.search(query, num);
			ScoreDoc[] sds = topDocs.scoreDocs;

			System.out.println(sds.length);
			for (ScoreDoc scoreDoc : sds) {
				Document document = indexSearcher.doc(scoreDoc.doc);
				System.out.println(String.format(
						"ID:%4d, score:%10s, film_id:%10s, title:%30s, length:%10s, date:%15s", scoreDoc.doc,
						scoreDoc.score, document.get("film_id"), document.get("title"), document.get("length"),
						new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(document.get("date"))))));
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

	// 范围查找
	public void searchByNumricRangeTerm(String field, int lValue, int hValue, int num) {
		IndexSearcher indexSearcher = null;
		try {
			indexSearcher = SearchUtil.getIndexSearcher();

			// 第四五个参数表示是否包括上下边界的值
			Query query = NumericRangeQuery.newIntRange(field, lValue, hValue, true, true);

			TopDocs topDocs = indexSearcher.search(query, num);
			System.out.println("一共查询到了 :" + topDocs.totalHits);
			ScoreDoc[] sds = topDocs.scoreDocs;

			System.out.println(sds.length);
			for (ScoreDoc scoreDoc : sds) {
				Document document = indexSearcher.doc(scoreDoc.doc);
				System.out.println(String.format(
						"ID:%4d, score:%10s, film_id:%10s, title:%30s, length:%10s, date:%15s", scoreDoc.doc,
						scoreDoc.score, document.get("film_id"), document.get("title"), document.get("length"),
						new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(document.get("date"))))));
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

	// 前缀搜索
	public void searchByPrefix(String field, String prefix, int num) {
		IndexSearcher indexSearcher = null;
		try {
			indexSearcher = SearchUtil.getIndexSearcher();

			Query query = new PrefixQuery(new Term(field, prefix));

			TopDocs topDocs = indexSearcher.search(query, num);
			System.out.println("一共查询到了 :" + topDocs.totalHits);
			ScoreDoc[] sds = topDocs.scoreDocs;

			System.out.println(sds.length);
			for (ScoreDoc scoreDoc : sds) {
				Document document = indexSearcher.doc(scoreDoc.doc);
				System.out.println(String.format(
						"ID:%4d, score:%10s, film_id:%10s, title:%30s, length:%10s, date:%15s", scoreDoc.doc,
						scoreDoc.score, document.get("film_id"), document.get("title"), document.get("length"),
						new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(document.get("date"))))));
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

	// 通配符搜索
	public void searchByWildcard(String field, String wildcard, int num) {
		IndexSearcher indexSearcher = null;
		try {
			indexSearcher = SearchUtil.getIndexSearcher();

			Query query = new WildcardQuery(new Term(field, wildcard));

			TopDocs topDocs = indexSearcher.search(query, num);
			System.out.println("一共查询到了 :" + topDocs.totalHits);
			ScoreDoc[] sds = topDocs.scoreDocs;

			System.out.println(sds.length);
			for (ScoreDoc scoreDoc : sds) {
				Document document = indexSearcher.doc(scoreDoc.doc);
				System.out.println(String.format(
						"ID:%4d, score:%10s, film_id:%10s, title:%30s, length:%10s, date:%15s", scoreDoc.doc,
						scoreDoc.score, document.get("film_id"), document.get("title"), document.get("length"),
						new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(document.get("date"))))));
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

	// 组合搜索
	public void searchByBoolean(int num) {
		IndexSearcher indexSearcher = null;
		try {
			indexSearcher = SearchUtil.getIndexSearcher();

			BooleanQuery query = new BooleanQuery();
			query.add(new PrefixQuery(new Term("title", "ace")), Occur.MUST);
			query.add(NumericRangeQuery.newIntRange("length", 10, 145, true, true), Occur.MUST_NOT);

			TopDocs topDocs = indexSearcher.search(query, num);
			System.out.println("一共查询到了 :" + topDocs.totalHits);
			ScoreDoc[] sds = topDocs.scoreDocs;

			System.out.println(sds.length);
			for (ScoreDoc scoreDoc : sds) {
				Document document = indexSearcher.doc(scoreDoc.doc);
				System.out.println(String.format(
						"ID:%4d, score:%10s, film_id:%10s, title:%30s, length:%10s, date:%15s", scoreDoc.doc,
						scoreDoc.score, document.get("film_id"), document.get("title"), document.get("length"),
						new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(document.get("date"))))));
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

		QueryParser parser = new MultiFieldQueryParser(Version.LUCENE_35, new String[] { "title", "content" },
				new StandardAnalyzer(Version.LUCENE_35));
		try {
			Query query = parser.parse("china");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 短语搜索, 当记不住一句话中的两个单词中间的单词的时候, 很好用
	public void searchByPhrase(int num) {
		IndexSearcher indexSearcher = null;
		try {
			indexSearcher = SearchUtil.getIndexSearcher();

			PhraseQuery query = new PhraseQuery();
			query.setSlop(1); // 设置跳过的单词数
			query.add(new Term("description", "madman")); // 设置开始的单词
			query.add(new Term("description", "must")); // 设置结束的单词

			TopDocs topDocs = indexSearcher.search(query, num);
			System.out.println("一共查询到了 :" + topDocs.totalHits);
			ScoreDoc[] sds = topDocs.scoreDocs;

			System.out.println(sds.length);
			for (ScoreDoc scoreDoc : sds) {
				Document document = indexSearcher.doc(scoreDoc.doc);
				System.out.println(String.format(
						"ID:%4d, score:%10s, film_id:%10s, title:%30s, length:%10s, date:%15s", scoreDoc.doc,
						scoreDoc.score, document.get("film_id"), document.get("title"), document.get("length"),
						new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(document.get("date"))))));
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

	// 模糊查询
	public void searchByFuzzy(int num) {
		IndexSearcher indexSearcher = null;
		try {
			indexSearcher = SearchUtil.getIndexSearcher();

			FuzzyQuery query = new FuzzyQuery(new Term("title", "earth"), 0.3f, 0);

			TopDocs topDocs = indexSearcher.search(query, num);
			System.out.println("一共查询到了 :" + topDocs.totalHits);
			ScoreDoc[] sds = topDocs.scoreDocs;

			System.out.println(sds.length);
			for (ScoreDoc scoreDoc : sds) {
				Document document = indexSearcher.doc(scoreDoc.doc);
				System.out.println(String.format(
						"ID:%4d, score:%10s, film_id:%10s, title:%30s, length:%10s, date:%15s", scoreDoc.doc,
						scoreDoc.score, document.get("film_id"), document.get("title"), document.get("length"),
						new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(document.get("date"))))));
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

	// 基于QueryParser的查询
	public void searchByQueryParser(int num) throws ParseException {
		IndexSearcher indexSearcher = null;
		try {
			indexSearcher = SearchUtil.getIndexSearcher();

			// 默认搜索description
			QueryParser parser = new QueryParser(Version.LUCENE_35, "description", new StandardAnalyzer(
					Version.LUCENE_35));
			parser.setDefaultOperator(Operator.AND);
			// dentist forensic之间有空格,表示有两个条件,条件是and还是or, 可以通过QueryParser.setDefaultOperator()方法指定
			Query query = parser.parse("dentist forensic");

			// 示例,通过冒号修改查询的field
			Query query1 = parser.parse("title:dawn");

			// 示例,通配符查询,问号和星号不能放在首位
			Query query2 = parser.parse("title:d*");

			// 示例,组合查询, 搜索title中不以d开头, description中包含forensic , 注意加减号周围的空格, 加减号放到field的
			Query query3 = parser.parse(" - title:d* +description:forensic");

			// 示例,范围查询,开区间, TO 必须是大写
			Query query4 = parser.parse("film_id:[10 TO 21]");
			// 示例,范围查询,闭区间, TO 必须是大写
			Query query5 = parser.parse("film_id:{10 TO 30}");

			// 示例:短语查询, 匹配多个连续的单词
			Query query6 = parser.parse("\"a Astounding Reflection\"");

			// 示例:短语查询, 匹配a 和 Reflection之间有一个单词的
			Query query7 = parser.parse("\"a Reflection\"~1");

			// 示例:模糊查询
			Query query8 = parser.parse("title:earth~");

			TopDocs topDocs = indexSearcher.search(query8, num);
			System.out.println("一共查询到了 :" + topDocs.totalHits);
			ScoreDoc[] sds = topDocs.scoreDocs;

			System.out.println(sds.length);
			for (ScoreDoc scoreDoc : sds) {
				Document document = indexSearcher.doc(scoreDoc.doc);
				System.out.println(String.format(
						"ID:%4d, score:%10s, film_id:%10s, title:%30s, length:%10s, date:%15s", scoreDoc.doc,
						scoreDoc.score, document.get("film_id"), document.get("title"), document.get("length"),
						new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(document.get("date"))))));
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

	// 分页,方法一
	public void searchForPageMethod1(String value, int pageIndex, int pageSize) {
		IndexSearcher indexSearcher = null;
		try {
			indexSearcher = SearchUtil.getIndexSearcher();

			// 默认搜索description
			QueryParser parser = new QueryParser(Version.LUCENE_35, "description", new StandardAnalyzer(
					Version.LUCENE_35));
			Query query = parser.parse(value);

			TopDocs topDocs = indexSearcher.search(query, 500);
			System.out.println("一共查询到了 :" + topDocs.totalHits);
			ScoreDoc[] sds = topDocs.scoreDocs;

			int start = (pageIndex - 1) * pageSize;
			int end = start + pageSize;

			System.out.println(sds.length);
			for (int i = start; i < end; i++) {
				Document document = indexSearcher.doc(sds[i].doc);
				System.out.println(document.get("film_id") + " " + document.get("title") + "  "
						+ document.get("length") + " " + document.get("date") + " " + sds[i].score);
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

	// 分页,方法二, Lucene 可以取出一个元素之后的指定数量的条目, 使用IndexSearcher.searchAfter()方法
	public void handSearchForPageMethod2() {
		IndexSearcher indexSearcher = null;
		ScoreDoc lastsDoc = null;
		ScoreDoc[] sds = null;
		try {
			indexSearcher = SearchUtil.getIndexSearcher();

			while (((sds = this.searchForPageMethod2(indexSearcher, lastsDoc, "dentist forensic", 10)) != null)
					&& (sds.length != 0)) {
				lastsDoc = sds[sds.length - 1];
				System.out.println("--------------------------------------------------------");

				for (ScoreDoc scoreDoc : sds) {
					Document document = indexSearcher.doc(scoreDoc.doc);
					System.out.println(String.format(
							"ID:%4d, score:%10s, film_id:%10s, title:%30s, length:%10s, date:%15s", scoreDoc.doc,
							scoreDoc.score, document.get("film_id"), document.get("title"), document.get("length"),
							new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(document.get("date"))))));
				}
			}
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public ScoreDoc[] searchForPageMethod2(IndexSearcher indexSearcher, ScoreDoc lastsDoc, String value, int pageSize) {
		try {
			// 默认搜索description
			QueryParser parser = new QueryParser(Version.LUCENE_35, "description", new StandardAnalyzer(
					Version.LUCENE_35));
			Query query = parser.parse(value);

			TopDocs topDocs = indexSearcher.searchAfter(lastsDoc, query, pageSize);
			ScoreDoc[] sds = topDocs.scoreDocs;
			return sds;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void sortSearch(String queryString, Sort sort) {
		IndexSearcher indexSearcher = null;
		try {
			indexSearcher = SearchUtil.getIndexSearcher();
			QueryParser parser = new QueryParser(Version.LUCENE_35, "description", new StandardAnalyzer(
					Version.LUCENE_35));
			Query query = parser.parse(queryString);

			TopDocs tds = null;
			if (sort != null) {
				tds = indexSearcher.search(query, 1000, sort);
			} else {
				tds = indexSearcher.search(query, 1000);
			}

			for (ScoreDoc sd : tds.scoreDocs) {
				Document document = indexSearcher.doc(sd.doc);
				System.out.println(String.format(
						"ID:%4d, score:%10s, film_id:%10s, title:%30s, length:%10s, date:%15s", sd.doc, sd.score,
						document.get("film_id"), document.get("title"), document.get("length"), document.get("date")));

			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public void filterSearch(String queryString, Filter filter) {
		IndexSearcher indexSearcher = null;
		try {
			indexSearcher = SearchUtil.getIndexSearcher();
			QueryParser parser = new QueryParser(Version.LUCENE_35, "description", new StandardAnalyzer(
					Version.LUCENE_35));
			Query query = parser.parse(queryString);

			TopDocs tds = null;
			if (filter != null) {
				tds = indexSearcher.search(query, filter, 1000);
			} else {
				tds = indexSearcher.search(query, 1000);
			}

			for (ScoreDoc sd : tds.scoreDocs) {
				Document document = indexSearcher.doc(sd.doc);
				System.out.println(String.format(
						"ID:%4d, score:%10s, film_id:%10s, title:%30s, length:%10s, date:%15s", sd.doc, sd.score,
						document.get("film_id"), document.get("title"), document.get("length"), document.get("date")));
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
