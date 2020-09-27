package com.andyadc.codeblocks.test;

import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author andy.an
 * @since 2018/5/22
 */
public class CryptoTest {

    public static String encode(String key, String data) throws Exception {
		Charset charset = StandardCharsets.UTF_8;
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(charset.encode(key).array(), "HmacSHA256");
        sha256_HMAC.init(secret_key);

		return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void testHmacSHA256() throws Exception {
        System.out.println(encode("secret_key", "123456"));
    }

}
