package com.andyadc.codeblocks.common.convert;

import com.andyadc.codeblocks.common.lang.StringUtils;

/**
 * The class to convert {@link String} to {@link Long}
 */
public class StringToLongConverter implements StringConverter<Long> {

	@Override
	public Long convert(String source) {
		return StringUtils.isNotEmpty(source) ? Long.valueOf(source) : null;
	}

	@Override
	public int getPriority() {
		return NORMAL_PRIORITY + 1;
	}
}
