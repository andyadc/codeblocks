package com.andyadc.codeblocks.kit.time;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 预编译模式
 */
public final class CachedDateFormatter {

	private static final Map<String, DateTimeFormatter> CACHE = new ConcurrentHashMap<>();

	public static DateTimeFormatter getFormatter(String pattern) {
		if (pattern == null || pattern.isEmpty()) return null;
		return CACHE.computeIfAbsent(pattern, DateTimeFormatter::ofPattern);
	}

}
