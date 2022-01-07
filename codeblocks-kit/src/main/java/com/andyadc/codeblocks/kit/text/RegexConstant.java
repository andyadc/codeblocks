package com.andyadc.codeblocks.kit.text;

import java.util.regex.Pattern;

/**
 * refer:
 * https://github.com/Blankj/AndroidUtilCode/blob/master/utilcode/src/main/java/com/blankj/utilcode/constant/RegexConstants.java#L11
 * https://github.com/Blankj/AndroidUtilCode/blob/master/utilcode/src/main/java/com/blankj/utilcode/util/RegexUtils.java
 *
 */
public final class RegexConstant {

    private RegexConstant() {
    }

    /* Regex of simple mobile. */
    private static final String REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$";
    public static final Pattern PATTERN_REGEX_MOBILE_SIMPLE = Pattern.compile(REGEX_MOBILE_SIMPLE);

    /**
     * Regex of exact mobile.
     * <p>
     * 移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188、198
     * </p>
     * <p>
     * 联通：130、131、132、145、155、156、166、171、175、176、185、186
     * </p>
     * <p>
     * 电信：133、153、173、177、180、181、189、199
     * </p>
     * <p>
     * 全球星：1349
     * </p>
     * <p>
     * 虚拟运营商：170
     * </p>
     */
    private static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(16[6])|(17[0,1,3,5-8])|(18[0-9])|(19[8,9]))\\d{8}$";
    public static final Pattern PATTERN_REGEX_MOBILE_EXACT = Pattern.compile(REGEX_MOBILE_EXACT);

    /**
     * Regex of id card number which length is 15.
     */
    private static final String REGEX_ID_CARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
    public static final Pattern PATTERN_REGEX_ID_CARD15 = Pattern.compile(REGEX_ID_CARD15);

    /**
     * Regex of id card number which length is 18.
     */
    private static final String REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";
    public static final Pattern PATTERN_REGEX_ID_CARD18 = Pattern.compile(REGEX_ID_CARD18);

    /**
     * Regex of email.
     */
    private static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    public static final Pattern PATTERN_REGEX_EMAIL = Pattern.compile(REGEX_EMAIL);

    /**
     * Regex of url.
     */
    private static final String REGEX_URL = "[a-zA-z]+://[^\\s]*";
    public static final Pattern PATTERN_REGEX_URL = Pattern.compile(REGEX_URL);

    /**
     * Regex of Chinese character.
     */
    private static final String REGEX_ZH = "^[\\u4e00-\\u9fa5]+$";
    public static final Pattern PATTERN_REGEX_ZH = Pattern.compile(REGEX_ZH);

    /**
     * Regex of ip address.
     */
    private static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
    public static final Pattern PATTERN_REGEX_IP = Pattern.compile(REGEX_IP);
}
