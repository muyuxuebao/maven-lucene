package com.yinliang.Lucene_3_5.custom_Filter;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.util.OpenBitSet;

public class MyFilter extends Filter {

	@Override
	public DocIdSet getDocIdSet(IndexReader reader) throws IOException {

		// 创建一个OpenBitSet, 长度为reader.maxDoc(), 默认里面元素为 0
		OpenBitSet obs = new OpenBitSet(reader.maxDoc());
		// obs.set(10); // 设置ID为 10的Document的对应的元素为1 ,表示通过过滤器
		obs.clear(10);// 设置ID为 10的Document的对应的元素为0 ,表示不能通过过滤器

		// obs.set(0, 100);// 设置ID从0到100的Document的对应的元素为1 ,表示通过过滤器

		return obs;
	}

}
