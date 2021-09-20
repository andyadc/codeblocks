package com.andyadc.codeblocks.common.convert;

import com.andyadc.codeblocks.common.lang.StringUtils;

/**
 * The class to convert {@link String} to {@link Short}
 */
public class StringToShortConverter implements StringConverter<Short> {

	@Override
	public Short convert(String source) {
		return StringUtils.isNotEmpty(source) ? Short.valueOf(source) : null;
	}

	@Override
	public int getPriority() {
		return NORMAL_PRIORITY + 2;
	}
}
