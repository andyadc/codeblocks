package com.andyadc.test.http;

import com.andyadc.codeblocks.common.annotation.NotNull;
import com.andyadc.codeblocks.kit.idgen.UUID;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.TlsVersion;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OkHttpTests {

	public void testCookie() {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.cookieJar(new CookieJar() {
			@Override
			public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {

			}

			@NotNull
			@Override
			public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
				Cookie cookie = new Cookie.Builder()
					.hostOnlyDomain(httpUrl.host())
					.name("session").value(UUID.randomUUID())
					.build();
				return Arrays.asList(cookie);
			}
		});
	}

	public static final MediaType MEDIA_TYPE_MARKDOWN
		= MediaType.parse("text/x-markdown; charset=utf-8");

	ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
		.tlsVersions(TlsVersion.TLS_1_2)
		.cipherSuites(
			CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
			CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
			CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
		.build();

	private final OkHttpClient client = new OkHttpClient.Builder()
		.connectionSpecs(Collections.singletonList(spec))
		.build();

	@Test
	public void run() throws Exception {


		String postBody = ""
			+ "Releases\n"
			+ "--------\n"
			+ "\n"
			+ " * _1.0_ May 6, 2013\n"
			+ " * _1.1_ June 15, 2013\n"
			+ " * _1.2_ August 11, 2013\n";

		Request request = new Request.Builder()
			.url("https://api.github.com/markdown/raw")
			.post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
			.build();

		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

			System.out.println(response.body().string());
		}
	}

	/**
	 * Supported Protocols
	 */
	@Test
	public void testSsl() throws Exception {
		SSLContext context = SSLContext.getInstance("TLS");

		context.init(null, null, null);

		SSLSocketFactory factory = context.getSocketFactory();

		SSLSocket socket = (SSLSocket) factory.createSocket();

		String[] protocols = socket.getSupportedProtocols();

		System.out.println("Supported Protocols: " + protocols.length);
		for (int i = 0; i < protocols.length; i++) {

			System.out.println(" " + protocols[i]);

		}
	}
}
