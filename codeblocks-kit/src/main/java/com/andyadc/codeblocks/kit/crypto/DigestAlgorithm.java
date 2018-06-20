package com.andyadc.codeblocks.kit.crypto;

/**
 * @author andy.an
 * @since 2018/6/20
 */
public enum DigestAlgorithm {

    MD5("MD5"),
    SHA256("SHA-256"),
    SHA384("SHA-384"),
    SHA512("SHA-512");

    private String value;

    DigestAlgorithm(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
