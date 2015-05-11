package com.yinliang.Lucene_3_5.custom_QueryParer;

import java.text.SimpleDateFormat;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.CharStream;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParserTokenManager;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.util.Version;

public class MyQueryParser extends QueryParser {

	protected MyQueryParser(CharStream stream) {
		super(stream);
		// TODO Auto-generated constructor stub
	}

	public MyQueryParser(QueryParserTokenManager tm) {
		super(tm);
		// TODO Auto-generated constructor stub
	}

	public MyQueryParser(Version matchVersion, String f, Analyzer a) {
		super(matchVersion, f, a);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected org.apache.lucene.search.Query getWildcardQuery(String field, String termStr) throws ParseException {
		throw new ParseException("由于性能问题, 已禁用了模糊查询");
	}

	@Override
	protected org.apache.lucene.search.Query getFuzzyQuery(String field, String termStr, float minSimilarity) throws ParseException {
		throw new ParseException("由于性能问题, 已禁用了通配符查询");
	}

	@Override
	protected org.apache.lucene.search.Query getRangeQuery(String field, String part1, String part2, boolean inclusive) throws ParseException {
		try {
			if (field.equals("length")) {
				return NumericRangeQuery.newIntRange(field, Integer.parseInt(part1), Integer.parseInt(part2), inclusive, inclusive);
			} else if (field.equals("date")) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				long start = sdf.parse(part1).getTime();
				long end = sdf.parse(part2).getTime();
				return NumericRangeQuery.newLongRange(field, start, end, inclusive, inclusive);
			}

			return super.getRangeQuery(field, part1, part2, inclusive);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
