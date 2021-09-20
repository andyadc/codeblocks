package com.andyadc.codeblocks.common.convert;

import com.andyadc.codeblocks.common.lang.StringUtils;

/**
 * The class to convert {@link String} to {@link Boolean}
 */
public class StringToBooleanConverter implements StringConverter<Boolean> {

	@Override
	public Boolean convert(String source) {
		return StringUtils.isNotEmpty(source) ? Boolean.valueOf(source) : null;
	}

	@Override
	public int getPriority() {
		return NORMAL_PRIORITY + 5;
	}
}
