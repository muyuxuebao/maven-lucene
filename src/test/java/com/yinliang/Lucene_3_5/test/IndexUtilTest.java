package com.yinliang.Lucene_3_5.test;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.yinliang.Lucene_3_5.indexer.IndexUtil;

public class IndexUtilTest {
	private IndexUtil filmUtil = new IndexUtil("film");

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	@Test
	public void testSearchIndex() {
		for (int i = 0; i < 10; i++) {
			this.filmUtil.searchIndex("Drama");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testCreateIndex() {
		this.filmUtil.createIndex();
	}

	@Test
	public void testUnDeleteAllIndex() {
		this.filmUtil.unDeleteAllIndex();
	}

	@Test
	public void testEmptyRecycleBin() {
		this.filmUtil.emptyRecycleBin();
	}

	@Test
	public void testNumOfIndex() {
		this.filmUtil.numOfIndex();
	}

	@Test
	public void testDeleteIndex() {
		this.filmUtil.deleteIndex(50);
	}

	@Test
	public void testMergeIndex() {
		this.filmUtil.mergeIndex(1);
	}
}
