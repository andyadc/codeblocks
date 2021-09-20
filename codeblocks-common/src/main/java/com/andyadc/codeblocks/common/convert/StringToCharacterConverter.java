package com.andyadc.codeblocks.common.convert;

import com.andyadc.codeblocks.common.lang.StringUtils;

/**
 * The class to convert {@link String} to {@link Character}
 */
public class StringToCharacterConverter implements StringConverter<Character> {

	@Override
	public Character convert(String source) {
		int length = StringUtils.length(source);
		if (length == 0) {
			return null;
		}
		if (length > 1) {
			throw new IllegalArgumentException("The source String is more than one character!");
		}
		return source.charAt(0);
	}

	@Override
	public int getPriority() {
		return NORMAL_PRIORITY + 8;
	}
}
