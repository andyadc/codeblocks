package com.andyadc.codeblocks.common.regex;

import java.util.regex.Pattern;

public enum RegularExpression {

	PHONE() {
		@Override
		boolean validate(String text) {
			return Pattern.matches(CustomPattern.PHONE_PATTERN.pattern(), text);
		}
	};

	abstract boolean validate(String text);
}
