package com.andyadc.codeblocks.common.regex;

import java.util.regex.Pattern;

public final class CustomPattern {

	static Pattern PHONE_PATTERN = Pattern.compile("^[1]([3-9])[0-9]{9}$");

}
