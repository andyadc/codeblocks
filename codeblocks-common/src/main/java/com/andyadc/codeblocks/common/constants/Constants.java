package com.andyadc.codeblocks.common.constants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;

/**
 * andy.an
 * 2019/12/6
 */
public interface Constants {

	int PROCESSOR_NUM = Runtime.getRuntime().availableProcessors();
	Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
	String DEFAULT_CHARSET_FORMAT = DEFAULT_CHARSET.name();
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
