package com.yinliang.Lucene_3_5.test;

import org.junit.Before;
import org.junit.Test;

import com.yinliang.Lucene_3_5.NRT.NRTIndexUtil;

public class NRTIndexUtilTest {
	NRTIndexUtil nrtIndexUtil = null;

	@Before
	public void init() {
		this.nrtIndexUtil = new NRTIndexUtil("academymanageIndex");
	}

	@Test
	public void createIndex() {
		this.nrtIndexUtil.createIndex();
	}

	@Test
	public void numOfIndex() {
		this.nrtIndexUtil.numOfIndex();
	}

	@Test
	public void deleteIndex() {
		this.nrtIndexUtil.deleteIndex(12);
		this.nrtIndexUtil.searchIndex();
	}

	@Test
	public void searchIndex() {
		this.nrtIndexUtil.searchIndex();
	}

	@Test
	public void searchIndex2() { // 模拟近实时搜索
		for (int i = 0; i < 10; i++) {
			if (i == 0) {
				this.nrtIndexUtil.deleteIndex(12);
			} else if (i == 4) {
				this.nrtIndexUtil.updateIndex(16);
			}

			this.nrtIndexUtil.searchIndex();
			System.out.println("------------------------------------------");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
