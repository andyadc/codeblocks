package com.andyadc.codeblocks.kit.text;

import java.util.regex.Pattern;

public class RegexUtil {

    private RegexUtil() {
    }

    /**
     * 特殊用户名校验, 只允许字母数字下划线, 不允许数字开头, 不允许下划线结尾
     */
    private static final String REGEX_USERNAME = "^[a-zA-Z][a-zA-Z0-9_]{4,15}(?<!_)$";
    private static final Pattern Pattern_REGEX_USERNAME = Pattern.compile(REGEX_USERNAME);

    public static boolean isUsername(CharSequence input) {
        return isMatch(Pattern_REGEX_USERNAME, input);
    }

    public static boolean isUrl(CharSequence input) {
        return isMatch(RegexConstant.PATTERN_REGEX_URL, input);
    }

    public static boolean isEmail(CharSequence input) {
        return isMatch(RegexConstant.PATTERN_REGEX_EMAIL, input);
    }

    public static boolean isIdCard(CharSequence input) {
        return isMatch(RegexConstant.PATTERN_REGEX_ID_CARD18, input)
                || isMatch(RegexConstant.PATTERN_REGEX_ID_CARD15, input);
    }

    public static boolean isMobileExact(CharSequence input) {
        return isMatch(RegexConstant.PATTERN_REGEX_MOBILE_EXACT, input);
    }

    public static boolean isMobileSimple(CharSequence input) {
        return isMatch(RegexConstant.PATTERN_REGEX_MOBILE_SIMPLE, input);
    }

    public static boolean isIp(CharSequence input) {
        return isMatch(RegexConstant.PATTERN_REGEX_IP, input);
    }

    public static boolean isMatch(Pattern pattern, CharSequence input) {
        return StringUtil.isNotEmpty(input) && pattern.matcher(input).matches();
    }
}
