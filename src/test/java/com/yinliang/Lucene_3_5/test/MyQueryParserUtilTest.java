package com.yinliang.Lucene_3_5.test;

import org.junit.Before;
import org.junit.Test;

import com.yinliang.Lucene_3_5.custom_QueryParer.MyQueryParserUtil;

public class MyQueryParserUtilTest {
	MyQueryParserUtil myQueryParserUtil = null;

	@Before
	public void init() {
		this.myQueryParserUtil = new MyQueryParserUtil("film");
	}

	@Test
	public void searchWildcard() {
		this.myQueryParserUtil.searchWildcard(1000);
	}

	@Test
	public void searchFuzzy() {
		this.myQueryParserUtil.searchFuzzy(1000);
	}

	@Test
	public void searchRange_Length() {
		this.myQueryParserUtil.searchRange_Length(1000);
	}

	@Test
	public void searchRange_Date() {
		this.myQueryParserUtil.searchRange_Date(1000);
	}

}
