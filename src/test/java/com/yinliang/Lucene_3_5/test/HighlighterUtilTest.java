package com.yinliang.Lucene_3_5.test;

import org.junit.Before;
import org.junit.Test;

import com.yinliang.Lucene_3_5.highlighter.HighlighterUtil;

public class HighlighterUtilTest {
	HighlighterUtil highlighterUtil = null;

	@Before
	public void init() {
		this.highlighterUtil = new HighlighterUtil();
	}

	@Test
	public void simpleDemo() {
		this.highlighterUtil.simpleDemo();
	}

	@Test
	public void createIndex() {
		this.highlighterUtil.createIndex();
	}

	@Test
	public void highlighterSearch() {
		this.highlighterUtil.highlighterSearch("title", "数据", 100);
	}

}
