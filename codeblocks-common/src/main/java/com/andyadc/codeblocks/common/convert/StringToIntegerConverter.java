package com.andyadc.codeblocks.common.convert;

import com.andyadc.codeblocks.common.lang.StringUtils;

/**
 * The class to convert {@link String} to {@link Integer}
 */
public class StringToIntegerConverter implements StringConverter<Integer> {

	@Override
	public Integer convert(String source) {
		return StringUtils.isNotEmpty(source) ? Integer.valueOf(source) : null;
	}

	@Override
	public int getPriority() {
		return NORMAL_PRIORITY;
	}
}
