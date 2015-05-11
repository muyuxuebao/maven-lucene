package com.yinliang.Lucene_3_5.test;

import org.apache.lucene.analysis.Analyzer;
import org.junit.Before;
import org.junit.Test;

import com.yinliang.Lucene_3_5.analyzer.AnalyzerUtil;
import com.yinliang.Lucene_3_5.analyzer.MyStopAnalyzer;

public class MyStopAnalyzerTest {
	private AnalyzerUtil analyzerUtil = null;

	@Before
	public void init() {
		this.analyzerUtil = new AnalyzerUtil();
	}

	@Test
	public void test() {
		String txt = "how are you thank you, I hate you";
		MyStopAnalyzer myStopAnalyzer = new MyStopAnalyzer(new String[] { "hate", "how" });// 忽略hate和how

		Analyzer[] analyzers = { myStopAnalyzer, new MyStopAnalyzer() };

		for (Analyzer analyzer : analyzers) {
			this.analyzerUtil.displayToken(txt, analyzer);
		}
	}
}
