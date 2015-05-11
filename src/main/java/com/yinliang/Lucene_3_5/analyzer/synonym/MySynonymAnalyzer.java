package com.yinliang.Lucene_3_5.analyzer.synonym;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MaxWordSeg;
import com.chenlb.mmseg4j.analysis.MMSegTokenizer;

public class MySynonymAnalyzer extends Analyzer {

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {

		Dictionary dic = Dictionary.getInstance("data");
		return new MySynonymFilter(new MMSegTokenizer(new MaxWordSeg(dic), reader));
	}

}

class MySynonymFilter extends TokenFilter {
	CharTermAttribute charTermAttribute = null;
	PositionIncrementAttribute positionIncrementAttribute = null;

	State currentState = null;
	Stack<String> synonymStack = null;

	protected MySynonymFilter(TokenStream input) {
		super(input);
		this.charTermAttribute = this.addAttribute(CharTermAttribute.class);
		this.positionIncrementAttribute = this.addAttribute(PositionIncrementAttribute.class);

		this.currentState = new State();

		this.synonymStack = new Stack<String>();
	}

	@Override
	public boolean incrementToken() throws IOException {
		if (this.synonymStack.size() > 0) {
			// 将元素出栈,并获取这个元素
			String str = this.synonymStack.pop();

			// 还原状态
			this.restoreState(this.currentState);
			this.charTermAttribute.setEmpty();
			this.charTermAttribute.append(str);

			// 设置位置0
			this.positionIncrementAttribute.setPositionIncrement(0);
			return true;
		}

		if (this.input.incrementToken() == false) {
			return false;
		}

		// 找同义词, 找到后放到栈(this.synonymStack)里面
		String[] synonyms = this.getSynonyms(this.charTermAttribute.toString());
		if (synonyms != null) {
			this.currentState = this.captureState();
			for (String string : synonyms) {
				this.synonymStack.push(string);
			}
		}
		return true;
	}

	private String[] getSynonyms(String name) {
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put("我", new String[] { "俺", "咱" });
		map.put("中国", new String[] { "天朝", "中华人民共和国" });
		return map.get(name);
	}
}
