package com.yinliang.Lucene_3_5.test;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.yinliang.Lucene_3_5.custom_score.MyScoreQuery;

public class MyScoreQueryTest {
	MyScoreQuery myScoreQuery = null;

	@Before
	public void init() {
		this.myScoreQuery = new MyScoreQuery("film");
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	@Test
	public void searchByCustomScore() {
		this.myScoreQuery.searchByCustomScore();
	}

	@Test
	public void searchByCustomScore_Title() {
		this.myScoreQuery.searchByCustomScore_Title();
	}

	@Test
	public void searchByCustomScore_Length() {
		this.myScoreQuery.searchByCustomScore_Length();
	}
}
