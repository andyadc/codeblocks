package com.andyadc.codeblocks.common.regex;

import java.util.regex.Pattern;

public enum RegularExpression {

	PHONE() {
		@Override
		public boolean validate(String text) {
			return isMatch(CustomPattern.PHONE_PATTERN, text);
		}
	};

	private static boolean isMatch(Pattern pattern, String input) {
		return (input != null) && pattern.matcher(input).matches();
	}

	public abstract boolean validate(String text);
}
