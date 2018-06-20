package com.andyadc.codeblocks.kit.crypto;

/**
 * Mac Algorithms
 * see: https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html
 *
 * @author andy.an
 * @since 2018/6/20
 */
public enum HmacAlgorithm {

    HmacMD5("HmacMD5"),
    HmacSHA1("HmacSHA1"),
    HmacSHA256("HmacSHA256"),
    HmacSHA384("HmacSHA384"),
    HmacSHA512("HmacSHA512");

    private String value;

    HmacAlgorithm(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
