package com.yinliang.Lucene_3_5.indexer;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

/*
 * Data come form the carried database of MySQL, it name is sakila.film
 */

public class IndexUtil {
	private static Directory directory = null;
	private static IndexReader indexReader = null;
	private static final ReentrantLock reentrantLock = new ReentrantLock();

	public IndexUtil(String path) {
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
			QueryParser queryParser = new QueryParser(Version.LUCENE_35, "description", new StandardAnalyzer(
					Version.LUCENE_35));
			Query query = queryParser.parse(description);
			TopDocs tds = indexSearcher.search(query, 100);
			ScoreDoc[] sds = tds.scoreDocs;
			System.out.println(sds.length);
			for (ScoreDoc scoreDoc : sds) {
				Document document = indexSearcher.doc(scoreDoc.doc);
				System.out.println(String.format(
						"ID:%4d, score:%10s, film_id:%10s, title:%30s, length:%10s, date:%15s", scoreDoc.doc,
						scoreDoc.score, document.get("film_id"), document.get("title"), document.get("length"),
						new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(document.get("date"))))));
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
		IndexWriter indexWriter = null;
		try {
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
			indexWriter = new IndexWriter(this.directory, iwc);
			Document doc = null;

			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/sakila?characterEncoding=UTF-8";
			Connection conn = DriverManager.getConnection(url, "root", "123456");
			String sql = "SELECT film_id ,title ,description ,length ,last_update FROM sakila.film";
			Statement stm = conn.createStatement();
			stm.execute(sql);
			ResultSet rs = stm.executeQuery(sql);
			while (rs.next()) {

				int film_id = rs.getInt(1);
				String title = rs.getString(2);
				String description = rs.getString(3);
				int length = rs.getInt(4);

				doc = new Document();
				doc.add(new Field("film_id", String.valueOf(film_id), Field.Store.YES,
						Field.Index.NOT_ANALYZED_NO_NORMS));
				doc.add(new Field("title", title, Field.Store.YES, Field.Index.ANALYZED));
				doc.add(new Field("description", description, Field.Store.YES, Field.Index.ANALYZED));
				doc.add(new NumericField("length", Field.Store.YES, true).setIntValue(length));
				doc.add(new NumericField("date", Field.Store.YES, true).setLongValue(new Date((long) ((Math.random()
						* Integer.MAX_VALUE * 10000))).getTime()));

				indexWriter.addDocument(doc);
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
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

	public void unDeleteAllIndex() {
		IndexReader indexReader = null;
		try {
			// In order to update index, the second parameter muse set as false to set the read only as false
			indexReader = IndexReader.open(this.directory, false);
			indexReader.undeleteAll();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (indexReader != null) {
				try {
					indexReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void emptyRecycleBin() {
		IndexWriter indexWriter = null;
		try {
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
			indexWriter = new IndexWriter(this.directory, iwc);

			indexWriter.forceMergeDeletes(); // 默认情况下,forceMergeDeletes 方法只有在被删除的doc 多于百分之十的时候才会清空回收站

		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (indexWriter != null) {
				try {
					indexWriter.close();
				} catch (CorruptIndexException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	public void numOfIndex() {
		IndexWriter indexWriter = null;
		IndexReader indexReader = null;

		try {
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
			indexWriter = new IndexWriter(this.directory, iwc);
			indexReader = IndexReader.open(this.directory, false);

			System.out.println("numDocs: " + indexWriter.numDocs());
			System.out.println("maxDoc: " + indexWriter.maxDoc());

			System.out.println("DeletedDocs: " + indexReader.numDeletedDocs());

		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (indexWriter != null) {
				try {
					indexWriter.close();
				} catch (CorruptIndexException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (indexReader != null) {
				try {
					indexReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void updateIndex() {
		IndexWriter indexWriter = null;
		try {
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
			indexWriter = new IndexWriter(this.directory, iwc);

			Document doc = new Document();
			doc.add(new Field("film_id", String.valueOf(11), Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
			doc.add(new Field("title", "afasdfasd", Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field("description", "fasfasdfsd", Field.Store.YES, Field.Index.ANALYZED));

			// Lucene 提供的更新索引的操作是先删除指定的索引,再重新插入, 并不会在原来的位置更新索引. 当ID 一样时,相当于在原来的位置更新索引
			indexWriter.updateDocument(new Term("film_id", String.valueOf(1)), new Document());
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (indexWriter != null) {
				try {
					indexWriter.close();
				} catch (CorruptIndexException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	public void mergeIndex(int num) {
		// 强制合并索引, 不推荐这么做, 因为这个操作会消耗大量的操作,会清空回收站 , 而且Lucene 会根据情况自动优化, 自动合并.
		IndexWriter indexWriter = null;

		try {
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
			indexWriter = new IndexWriter(this.directory, iwc);

			indexWriter.forceMerge(num);
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (indexWriter != null) {
				try {
					indexWriter.close();
				} catch (CorruptIndexException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	public void deleteIndex(int film_id) {
		IndexWriter indexWriter = null;

		try {
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
			indexWriter = new IndexWriter(this.directory, iwc);

			// 参数可以是一个Qurey,也可以是一个Term, Term提供精确查找的值.
			indexWriter.deleteDocuments(new Term("film_id", String.valueOf(film_id)));
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (indexWriter != null) {
				try {
					indexWriter.close();
				} catch (CorruptIndexException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		}
	}

}
