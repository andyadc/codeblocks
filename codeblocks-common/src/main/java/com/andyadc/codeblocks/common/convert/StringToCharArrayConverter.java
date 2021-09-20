package com.andyadc.codeblocks.common.convert;

import com.andyadc.codeblocks.common.lang.StringUtils;

/**
 * The class to convert {@link String} to <code>char[]</code>
 */
public class StringToCharArrayConverter implements StringConverter<char[]> {

	@Override
	public char[] convert(String source) {
		return StringUtils.isNotEmpty(source) ? source.toCharArray() : null;
	}

	@Override
	public int getPriority() {
		return NORMAL_PRIORITY + 7;
	}
}
