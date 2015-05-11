package com.yinliang.Lucene_3_5.test;

import static org.junit.Assert.fail;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.NumericRangeFilter;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.util.Version;
import org.junit.Before;
import org.junit.Test;

import com.yinliang.Lucene_3_5.searcher.SearchUtil;

public class SearchUtilTest {
	private SearchUtil filmSearchUtil = null;

	@Before
	public void init() {
		this.filmSearchUtil = new SearchUtil("film");
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	@Test
	public void searchByTeam() {
		this.filmSearchUtil.searchByTerm("description", "dentist", 100);
	}

	@Test
	public void searchByRangeTerm() {
		this.filmSearchUtil.searchByRangeTerm("film_id", "1", "50", 500);
	}

	@Test
	public void searchByNumricRangeTerm() {
		this.filmSearchUtil.searchByNumricRangeTerm("length", 50, 100, 100);
	}

	@Test
	public void searchByPrefix() {
		this.filmSearchUtil.searchByPrefix("title", "a", 10);
	}

	@Test
	public void searchByWildcard() {
		this.filmSearchUtil.searchByWildcard("title", "a??", 10);
	}

	@Test
	public void searchByBoolean() {
		this.filmSearchUtil.searchByBoolean(10);
	}

	@Test
	public void searchByPhrase() {
		this.filmSearchUtil.searchByPhrase(10);
	}

	@Test
	public void searchByFuzzy() {
		this.filmSearchUtil.searchByFuzzy(10);
	}

	@Test
	public void searchByQueryParser() {
		try {
			this.filmSearchUtil.searchByQueryParser(10);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void searchForPateMethod1() {
		this.filmSearchUtil.searchForPageMethod1("dentist forensic", 1, 10);
	}

	@Test
	public void searchForPateMethod2() {
		this.filmSearchUtil.handSearchForPageMethod2();
	}

	@Test
	public void sortSearch() {
		// 默认根据评分排序
		// this.filmSearchUtil.sortSearch("dentist forensic", null);

		// 根据ID排序
		// this.filmSearchUtil.sortSearch("dentist forensic", Sort.INDEXORDER);

		// 根据Field排序
		// this.filmSearchUtil.sortSearch("dentist forensic", new Sort(new SortField("length", SortField.INT)));
		// this.filmSearchUtil.sortSearch("dentist forensic", new Sort(new SortField("length", SortField.INT, true))); // 降序
		// this.filmSearchUtil.sortSearch("dentist forensic", new Sort(new SortField("date", SortField.LONG)));// 根据日期排序
		// this.filmSearchUtil.sortSearch("dentist forensic", new Sort(new SortField("title", SortField.STRING)));// 根据String排序

		this.filmSearchUtil.sortSearch("dentist forensic", new Sort(new SortField("title", SortField.STRING),
				new SortField("length", SortField.INT)));// 多个Field排序
	}

	@Test
	public void filterSearch() throws ParseException {

		// 默认
		Filter filter1 = null;

		// length从 80 到 100 的
		Filter filter2 = NumericRangeFilter.newIntRange("length", 80, 100, true, true);

		// 添加新的Field的过滤
		Filter filter3 = new QueryWrapperFilter(new QueryParser(Version.LUCENE_35, "title", new StandardAnalyzer(
				Version.LUCENE_35)).parse("circus"));

		this.filmSearchUtil.filterSearch("dentist forensic", filter3);
	}

}
