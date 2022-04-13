package com.andyadc.bms.test;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * windows:keytool -genkey -alias jwt -keyalg RSA -keysize 1024 -keystore jwt.jks -validity 365
 * windows:keytool -importkeystore -srckeystore jwt.jks -destkeystore jwt.jks -deststoretype pkcs12
 */
public class JksTests {

	public static void main(String[] args) throws Exception {
		PrivateKey privateKey = getPrivateKey("jwt.jks", "123465", "jwt");
		System.out.println(privateKey);
		PublicKey publicKey = getPublicKey("jwt.jks", "123465", "jwt");
		System.out.println(publicKey);
	}

	private static PrivateKey getPrivateKey(String fileName, String password, String alias) throws Exception {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);

		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(inputStream, password.toCharArray());

		return (PrivateKey) keyStore.getKey(alias, password.toCharArray());
	}

	private static PublicKey getPublicKey(String fileName, String password, String alias) throws Exception {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);

		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(inputStream, password.toCharArray());

		return keyStore.getCertificate(alias).getPublicKey();
	}
}
