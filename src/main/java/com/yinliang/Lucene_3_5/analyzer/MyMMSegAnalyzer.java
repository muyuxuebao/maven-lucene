package com.yinliang.Lucene_3_5.analyzer;

import com.chenlb.mmseg4j.*;
import com.chenlb.mmseg4j.analysis.MMSegTokenizer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

public final class MyMMSegAnalyzer extends Analyzer {
    private Set<Object> stops = new HashSet<Object>();

    public MyMMSegAnalyzer(File path, String sws[]) {// 此构造方法构造出来的Analyzer除了与StopAnalyzer拥有相同的搜索时忽略的词汇外,还有自定义的搜索时被忽略的词汇,这些词汇由sws数组传入
        this.stops = StopFilter.makeStopSet(Version.LUCENE_35, sws, true);
        this.stops.addAll(StopAnalyzer.ENGLISH_STOP_WORDS_SET);// StopAnalyzer中默认的英文的停用词(搜索时忽略的词汇)
        this.dic = Dictionary.getInstance(path);
    }

    @Override
    public final TokenStream tokenStream(String fieldName, Reader reader) {
        MMSegTokenizer ts = new MMSegTokenizer(this.newSeg(), reader);
        StopFilter stopFilter = new StopFilter(Version.LUCENE_35, ts, this.stops);
        return stopFilter;
    }

    protected Seg newSeg() {
        return new ComplexSeg(this.dic);
    }

    protected Dictionary dic;
}
