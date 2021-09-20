package com.andyadc.codeblocks.common.convert;

import com.andyadc.codeblocks.common.lang.StringUtils;

/**
 * The class to convert {@link String} to {@link Float}
 */
public class StringToFloatConverter implements StringConverter<Float> {

	@Override
	public Float convert(String source) {
		return StringUtils.isNotEmpty(source) ? Float.valueOf(source) : null;
	}

	@Override
	public int getPriority() {
		return NORMAL_PRIORITY + 4;
	}
}
