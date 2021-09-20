package com.andyadc.codeblocks.common.convert;

import com.andyadc.codeblocks.common.lang.StringUtils;

/**
 * The class to convert {@link String} to {@link Double}
 */
public class StringToDoubleConverter implements StringConverter<Double> {

	@Override
	public Double convert(String source) {
		return StringUtils.isNotEmpty(source) ? Double.valueOf(source) : null;
	}

	@Override
	public int getPriority() {
		return NORMAL_PRIORITY + 3;
	}
}
