package com.andyadc.codeblocks.patchca.utils;

public final class Utils {

	public static <T extends CharSequence> T defaultIfBlank(final T str, final T defaultStr) {
		return isBlank(str) ? defaultStr : str;
	}

	public static boolean isNotBlank(final CharSequence cs) {
		return !isBlank(cs);
	}

	public static boolean isBlank(final CharSequence str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}

