package com.yinliang.Lucene_3_5.custom_Filter;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.util.OpenBitSet;

public class TitleFilter extends Filter {

	@Override
	public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
		// 创建一个OpenBitSet, 长度为reader.maxDoc(), 默认里面元素为 0
		OpenBitSet obs = new OpenBitSet(reader.maxDoc());
		/*
		 * 函数简介
		 */
		// obs.set(10); // 设置ID为 10的Document的对应的元素为1 ,表示通过过滤器
		// obs.clear(10); // 设置ID为 10的Document的对应的元素为0 ,表示不能通过过滤器
		// obs.set(0, 100);// 设置ID从0到100的Document的对应的元素为1 ,表示通过过滤器

		/************************************************************************/

		/*
		 * 过滤掉离距离现在的时间超过一年的document
		 */

		// 首先选择所有的document
		obs.set(0, reader.maxDoc());

		int docs[] = new int[1000];
		int freqs[] = new int[1000];

		String[] titles = new String[] { "ARSENIC", "INDEPENDENCE", "sex", "GUN", "PLUTO", "HOMICIDE", "LUST", "DAWN" };
		for (String string : titles) {
			TermDocs tds = reader.termDocs(new Term("title", string.toLowerCase()));
			int count = tds.read(docs, freqs);

			for (int i = 0; i < count; i++) {
				obs.clear(docs[i]);
			}
		}

		return obs;
	}
}
