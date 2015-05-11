package com.yinliang.Lucene_3_5.analyzer;

import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LetterTokenizer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.Version;

public class MyStopAnalyzer extends Analyzer {
	private Set<Object> stops = new HashSet<Object>();

	public MyStopAnalyzer() { // 此构造方法构造出来的Analyzer与StopAnalyzer拥有相同的搜索时忽略的词汇
		this.stops.addAll(StopAnalyzer.ENGLISH_STOP_WORDS_SET);// StopAnalyzer中默认的英文的停用词(搜索时忽略的词汇)
	}

	public MyStopAnalyzer(String sws[]) {// 此构造方法构造出来的Analyzer除了与StopAnalyzer拥有相同的搜索时忽略的词汇外,还有自定义的搜索时被忽略的词汇,这些词汇由sws数组传入
		this.stops = StopFilter.makeStopSet(Version.LUCENE_35, sws, true);
		this.stops.addAll(StopAnalyzer.ENGLISH_STOP_WORDS_SET);// StopAnalyzer中默认的英文的停用词(搜索时忽略的词汇)
	}

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {

		// 多个Filter嵌套
		return new StopFilter(Version.LUCENE_35, new LowerCaseFilter(Version.LUCENE_35, new LetterTokenizer(Version.LUCENE_35, reader)), this.stops);
	}
}
