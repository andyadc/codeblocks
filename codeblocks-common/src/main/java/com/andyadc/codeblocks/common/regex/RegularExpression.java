package com.andyadc.codeblocks.common.regex;

import java.util.regex.Pattern;

public enum RegularExpression {

	PHONE() {
		@Override
		public boolean validate(String text) {
			return Pattern.matches(CustomPattern.PHONE_PATTERN.pattern(), text);
		}
	};

	public abstract boolean validate(String text);
}
