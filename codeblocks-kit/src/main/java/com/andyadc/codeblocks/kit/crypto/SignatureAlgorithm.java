package com.andyadc.codeblocks.kit.crypto;

/**
 * @author andy.an
 * @since 2018/6/20
 */
public enum SignatureAlgorithm {

    // The signature algorithm with SHA-* and the RSA encryption algorithm
    SHA1withRSA("SHA1withRSA"),
    SHA224withRSA("SHA224withRSA"),
    SHA256withRSA("SHA256withRSA"),
    SHA384withRSA("SHA384withRSA"),
    SHA512withRSA("SHA512withRSA"),

    // The DSA signature algorithms that use the SHA-1, SHA-224, or SHA-256 digest algorithms
    SHA1withDSA("SHA1withDSA"),
    SHA224withDSA("SHA224withDSA"),
    SHA256withDSA("SHA256withDSA");

    private String value;

    SignatureAlgorithm(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
