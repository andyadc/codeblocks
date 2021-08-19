package com.andyadc.codeblocks.common.constants;

import java.time.ZoneId;

/**
 * andy.an
 * 2019/12/6
 */
public interface Constants {

	int PROCESSOR_NUM = Runtime.getRuntime().availableProcessors();
	String DEFAULT_CHARSET = "UTF-8";
	ZoneId DEFAULT_ZONE_ID = ZoneId.of("Asia/Shanghai");

	/**
	 * Dot : "."
	 */
	String DOT = ".";

	/**
	 * Class : "class"
	 */
	String CLASS = "class";

	/**
	 * And : "&"
	 */
	String AND = "&";

	/**
	 * Equal : "="
	 */
	String EQUAL = "=";
}
