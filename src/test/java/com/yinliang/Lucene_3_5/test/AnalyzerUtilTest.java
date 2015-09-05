package com.yinliang.Lucene_3_5.test;

import static org.junit.Assert.fail;

import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;
import org.junit.Before;
import org.junit.Test;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;
import com.yinliang.Lucene_3_5.analyzer.AnalyzerUtil;

public class AnalyzerUtilTest {
	private AnalyzerUtil analyzerUtil = null;

	@Before
	public void init() {
		this.analyzerUtil = new AnalyzerUtil();
	}

	@Test
	public void displayToken_01() {
		Analyzer[] analyzers = { new StandardAnalyzer(Version.LUCENE_35), new StopAnalyzer(Version.LUCENE_35), new SimpleAnalyzer(Version.LUCENE_35), new WhitespaceAnalyzer(Version.LUCENE_35) };

		String txt = "this is my house, i come form anhui, china";

		for (Analyzer analyzer : analyzers) {
			this.analyzerUtil.displayToken(txt, analyzer);
		}
	}

	@Test
	public void displayToken_02() {
		Analyzer[] analyzers = { new MMSegAnalyzer(new File("data")), new StandardAnalyzer(Version.LUCENE_35), new StopAnalyzer(Version.LUCENE_35), new SimpleAnalyzer(Version.LUCENE_35), new WhitespaceAnalyzer(Version.LUCENE_35) };

		String txt = "这是我的家, 我来自中国安徽省六安市苍墩村";


		int aaa = 1000;
		System.out.println(aaa);
		for (Analyzer analyzer : analyzers) {
			this.analyzerUtil.displayToken(txt, analyzer);
		}
	}

	@Test
	public void displayToken_03() {
		Analyzer[] analyzers = { new StandardAnalyzer(Version.LUCENE_35), new StopAnalyzer(Version.LUCENE_35), new SimpleAnalyzer(Version.LUCENE_35), new WhitespaceAnalyzer(Version.LUCENE_35) };

		String txt = "how are you thank you";

		for (Analyzer analyzer : analyzers) {
			this.analyzerUtil.dispalyAllTokenInfo(txt, analyzer);
		}
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
