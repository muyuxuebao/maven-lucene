package com.yinliang.Lucene_3_5.test;

import org.junit.Before;
import org.junit.Test;

import com.yinliang.Lucene_3_5.custom_Filter.MyFilterUtil;

public class MyFilterUtilTest {
	MyFilterUtil myFilterUtilTest = null;

	@Before
	public void Init() {
		this.myFilterUtilTest = new MyFilterUtil("film");
	}

	@Test
	public void test() {
		this.myFilterUtilTest.search("description", "dentist", 1000);
	}

}
