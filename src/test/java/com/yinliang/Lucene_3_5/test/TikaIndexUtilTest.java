package com.yinliang.Lucene_3_5.test;

import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.yinliang.Lucene_3_5.tika.TikaIndexUtil;

public class TikaIndexUtilTest {
	private TikaIndexUtil tikaIndexUtil = null;

	@Before
	public void init() {
		this.tikaIndexUtil = new TikaIndexUtil("film");
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	@Test
	public void convertFileToString() {
		System.out.println(this.tikaIndexUtil.convertFileToString(new File("word/齐天大圣孙徟空.doc")));
	}

	@Test
	public void convertFileToString2() {
		System.out.println(this.tikaIndexUtil.convertFileToString2(new File("pdf/齐天大圣孙徟空.pdf")));
	}

}
