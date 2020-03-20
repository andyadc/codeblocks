package com.andyadc.codeblocks.showcase.common.db;

import com.p6spy.engine.common.P6Util;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

/**
 * andy.an
 * 2020/3/18
 */
public class CustomizationFormat implements MessageFormattingStrategy {

	@Override
	public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
		return "|" + elapsed + "ms|\n" + P6Util.singleLine(sql);
	}
}
