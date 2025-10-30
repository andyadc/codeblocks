package com.andyadc.test.http.claude;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.security.KeyStore;

public final class TlsConfig {

	public static SSLConnectionSocketFactory sslSocketFactoryWithTrustStore(String truststorePath, String password) throws Exception {
		KeyStore trustStore = KeyStore.getInstance("JKS");
		try (FileInputStream fis = new FileInputStream(truststorePath)) {
			trustStore.load(fis, password.toCharArray());
		}
		SSLContext sslContext = SSLContexts.custom()
			.loadTrustMaterial(trustStore, null) // or new TrustSelfSignedStrategy() if appropriate
			.build();

		return new SSLConnectionSocketFactory(
			sslContext,
			new String[] {"TLSv1.2", "TLSv1.3"},
			null,
			SSLConnectionSocketFactory.getDefaultHostnameVerifier()
		);
	}

	public static SSLConnectionSocketFactory sslSocketFactoryWithMutualTLS(
		String keystorePath, String keyPass, String truststorePath, String trustPass) throws Exception {

		KeyStore keyStore = KeyStore.getInstance("PKCS12"); // or "JKS"
		try (FileInputStream kfis = new FileInputStream(keystorePath)) {
			keyStore.load(kfis, keyPass.toCharArray());
		}

		KeyStore trustStore = KeyStore.getInstance("JKS");
		try (FileInputStream tfis = new FileInputStream(truststorePath)) {
			trustStore.load(tfis, trustPass.toCharArray());
		}

		SSLContext sslContext = SSLContexts.custom()
			.loadKeyMaterial(keyStore, keyPass.toCharArray())     // client cert
			.loadTrustMaterial(trustStore, null)                  // trust anchors
			.build();

		return new SSLConnectionSocketFactory(
			sslContext,
			new String[] {"TLSv1.2", "TLSv1.3"},
			null,
			SSLConnectionSocketFactory.getDefaultHostnameVerifier()
		);
	}
}
