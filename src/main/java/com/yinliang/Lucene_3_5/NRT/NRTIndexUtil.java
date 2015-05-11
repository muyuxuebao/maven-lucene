package com.yinliang.Lucene_3_5.NRT;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.NRTManagerReopenThread;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.SearcherWarmer;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

/*
 * Data come form the carried database of MySQL, it name is sakila.film
 */

public class NRTIndexUtil {
	private Directory directory = null;

	NRTManager nrtManager = null;
	NRTManagerReopenThread nrtManagerReopenThread = null;
	SearcherManager searcherManager = null;
	IndexWriter indexWriter = null;

	public NRTIndexUtil(String path) {
		try {
			this.directory = FSDirectory.open(new File(path));
			this.indexWriter = new IndexWriter(this.directory, new IndexWriterConfig(Version.LUCENE_35,
					new StandardAnalyzer(Version.LUCENE_35)));

			this.nrtManager = new NRTManager(this.indexWriter, new SearcherWarmer() {

				public void warm(IndexSearcher s) throws IOException {
					System.out.println("reopen");
				}
			});

			this.nrtManagerReopenThread = new NRTManagerReopenThread(this.nrtManager, 5.0, 0.025);
			this.nrtManagerReopenThread.setDaemon(true);
			this.nrtManagerReopenThread.setName("NRTManager reopen thread");
			this.nrtManagerReopenThread.start();

			this.searcherManager = this.nrtManager.getSearcherManager(true);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addDocument() {
		Document doc = new Document();
		doc.add(new Field("student_id", String.valueOf(999), Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("name", "你妈比", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("sex", "男", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("birthDay", "999", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("cellphoneNumber", "4124123", Field.Store.YES, Field.Index.NOT_ANALYZED));

		try {
			this.nrtManager.addDocument(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createIndex() {
		try {
			int student_ids[] = new int[] { 12, 13, 14, 15, 16, 18 };
			Document doc = null;
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/sakila?characterEncoding=UTF-8";
			Connection conn = DriverManager.getConnection(url, "root", "123456");
			String sql = "SELECT student_id, name, sex, birthDay, cellphoneNumber FROM academymanage.studentinfo where student_id = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);

			for (int i = 0; i < student_ids.length; i++) {
				preparedStatement.setInt(1, student_ids[i]);
				ResultSet rs = preparedStatement.executeQuery();
				rs.next();
				int student_id = rs.getInt(1);
				String name = rs.getString(2);
				String sex = rs.getString(3);
				String birthDay = rs.getString(4);
				String cellphoneNumber = rs.getString(5);

				doc = new Document();

				doc.add(new Field("student_id", String.valueOf(student_id), Field.Store.YES, Field.Index.NOT_ANALYZED));
				doc.add(new Field("name", name, Field.Store.YES, Field.Index.NOT_ANALYZED));
				doc.add(new Field("sex", sex, Field.Store.YES, Field.Index.NOT_ANALYZED));
				doc.add(new Field("birthDay", birthDay, Field.Store.YES, Field.Index.NOT_ANALYZED));
				doc.add(new Field("cellphoneNumber", cellphoneNumber, Field.Store.YES, Field.Index.NOT_ANALYZED));

				this.nrtManager.addDocument(doc);
			}
			this.indexWriter.commit();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void updateIndex(int student_id) {
		try {
			Document doc = new Document();
			doc.add(new Field("student_id", String.valueOf(student_id), Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field("sex", "男", Field.Store.YES, Field.Index.NOT_ANALYZED));

			this.nrtManager.updateDocument(new Term("student_id", String.valueOf(12)), doc);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteIndex(int student_id) {

		try {
			this.nrtManager.deleteDocuments(new Term("student_id", String.valueOf(student_id)));
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

	public void searchIndex() {
		IndexSearcher indexSearcher = this.searcherManager.acquire();
		try {

			// 4.创建Parser来确定要搜索的文件的内容,第二个蚕食表示搜索的域
			QueryParser queryParser = new QueryParser(Version.LUCENE_35, "sex", new StandardAnalyzer(Version.LUCENE_35));
			Query query = queryParser.parse("男");
			TopDocs tds = indexSearcher.search(query, 100);
			ScoreDoc[] sds = tds.scoreDocs;
			System.out.println(sds.length);
			for (ScoreDoc scoreDoc : sds) {
				Document document = indexSearcher.doc(scoreDoc.doc);
				System.out.println(String.format("student_id:%10s, sex:%10s, birthDay:%15s",
						document.get("student_id"), document.get("sex"), document.get("birthDay")));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (indexSearcher != null) {
				try {
					this.searcherManager.release(indexSearcher);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void numOfIndex() {
		IndexSearcher indexSearcher = this.searcherManager.acquire();
		IndexReader indexReader = indexSearcher.getIndexReader();

		System.out.println("numDocs: " + indexReader.numDocs());
		System.out.println("maxDoc: " + indexSearcher.maxDoc());

		System.out.println("DeletedDocs: " + indexReader.numDeletedDocs());

	}
}
