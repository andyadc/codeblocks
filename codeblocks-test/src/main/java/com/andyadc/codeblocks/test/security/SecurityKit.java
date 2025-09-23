package com.andyadc.codeblocks.test.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

public class SecurityKit {

	private static final Logger logger = LoggerFactory.getLogger(SecurityKit.class);

	public static boolean verify(String algorithm, byte[] data, PublicKey publicKey, byte[] signature) {
		try {
			Signature sign = Signature.getInstance(algorithm);
			sign.initVerify(publicKey);
			sign.update(data);
			return sign.verify(signature);
		} catch (NoSuchAlgorithmException e) {
			logger.warn("NoSuchAlgorithm: {}", algorithm);
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			logger.warn("InvalidKey: {}", publicKey);
			throw new RuntimeException(e);
		} catch (SignatureException e) {
			logger.warn("Signature update error");
			throw new RuntimeException(e);
		}
	}

	public static byte[] sign(String algorithm, byte[] data, PrivateKey privateKey) {
		try {
			Signature signature = Signature.getInstance(algorithm);
			signature.initSign(privateKey);
			signature.update(data);
			return signature.sign();
		} catch (NoSuchAlgorithmException e) {
			logger.warn("NoSuchAlgorithm: {}", algorithm);
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			logger.warn("InvalidKey: {}", privateKey);
			throw new RuntimeException(e);
		} catch (SignatureException e) {
			logger.warn("Signature sign error");
			throw new RuntimeException(e);
		}
	}

}
