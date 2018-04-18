package com.andyadc.codeblocks.mybatis.util;

/**
 * @author andaicheng
 * @version 1.0, 2016-10-07
 */
public final class StringUtils {

    private StringUtils() {
    }

    public static boolean isEmpty(final CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

    public static <T extends CharSequence> T defaultIfEmpty(final T str, final T defaultStr) {
        return isEmpty(str) ? defaultStr : str;
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

    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    public static <T extends CharSequence> T defaultIfBlank(final T str, final T defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }

}
