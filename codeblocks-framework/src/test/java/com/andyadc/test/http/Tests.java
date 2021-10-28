package com.andyadc.test.http;

import com.andyadc.codeblocks.common.constants.Constants;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;

public class Tests {

	protected static final String CONTENT_TYPE_JSON = "application/json"; // default
	protected static final String CONTENT_TYPE_JSON_PATTERN = CONTENT_TYPE_JSON + "; charset={0}";// default

	@Test
	public void testStringFormat() {
		String format = MessageFormat.format(CONTENT_TYPE_JSON_PATTERN, Constants.DEFAULT_CHARSET);
		System.out.println(format);
	}
}
