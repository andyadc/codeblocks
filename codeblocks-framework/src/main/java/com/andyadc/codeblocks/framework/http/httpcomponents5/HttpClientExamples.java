package com.andyadc.codeblocks.framework.http.httpcomponents5;

import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.pool.PoolStats;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.Timeout;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

public class HttpClientExamples {

	public static void main(String[] args) throws Exception {
		// Basic Configuration
		basicConfiguration();

		// Authentication
		basicAuthentication();
		bearerTokenAuthentication();
		cookieHandling();

		// Request/Response Handling
		httpPostJson();
		multipartFileUpload();
	}

	/**
	 * Demonstrates the basic configuration of HttpClient.
	 */
	public static void basicConfiguration() throws Exception {
		// Create a connection manager with custom pooling settings
		PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
			.setMaxConnTotal(100)
			.setMaxConnPerRoute(20)
			.build();

		// Create a request configuration with timeouts
		RequestConfig requestConfig = RequestConfig.custom()
			.setConnectTimeout(Timeout.ofSeconds(5))
			.setConnectionRequestTimeout(Timeout.ofSeconds(5))
			.setResponseTimeout(Timeout.ofSeconds(5))
			.build();

		// Configure a proxy
		HttpHost proxy = new HttpHost("http", "proxy.example.com", 8080);

		// Configure SSL/TLS with a custom trust store
		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		try (FileInputStream instream = new FileInputStream(new File("my.truststore"))) {
			trustStore.load(instream, "mypassword".toCharArray());
		}
		SSLContext sslContext = SSLContexts.custom()
			.loadTrustMaterial(trustStore, null)
			.build();
		SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext);

		try (CloseableHttpClient httpClient = HttpClients.custom()
			.setConnectionManager(connectionManager)
			.setDefaultRequestConfig(requestConfig)
			.setProxy(proxy)
//			.setSSLSocketFactory(sslSocketFactory)
			.build()) {

			// Execute a request
			HttpGet httpGet = new HttpGet("https://api.example.com/data");
			httpClient.execute(httpGet, response -> {
				System.out.println("Status: " + response.getCode());
				EntityUtils.consume(response.getEntity());
				return null;
			});

			// Monitor the connection pool
			PoolStats stats = connectionManager.getTotalStats();
			System.out.println("Connection pool stats: " + stats);
		}
	}

	/**
	 * Demonstrates basic authentication.
	 */
	public static void basicAuthentication() throws Exception {
		BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(
			new AuthScope("https://api.example.com", 443),
			new UsernamePasswordCredentials("user", "password".toCharArray()));

		try (CloseableHttpClient httpClient = HttpClients.custom()
			.setDefaultCredentialsProvider(credentialsProvider)
			.build()) {
			HttpGet httpGet = new HttpGet("https://api.example.com/secure/data");
			httpClient.execute(httpGet, response -> {
				System.out.println("Status: " + response.getCode());
				EntityUtils.consume(response.getEntity());
				return null;
			});
		}
	}

	/**
	 * Demonstrates bearer token authentication.
	 */
	public static void bearerTokenAuthentication() throws Exception {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpGet httpGet = new HttpGet("https://api.example.com/data");
			httpGet.setHeader("Authorization", "Bearer my-token");
			httpClient.execute(httpGet, response -> {
				System.out.println("Status: " + response.getCode());
				EntityUtils.consume(response.getEntity());
				return null;
			});
		}
	}

	/**
	 * Demonstrates cookie handling.
	 */
	public static void cookieHandling() throws Exception {
		BasicCookieStore cookieStore = new BasicCookieStore();
		try (CloseableHttpClient httpClient = HttpClients.custom()
			.setDefaultCookieStore(cookieStore)
			.build()) {
			HttpGet httpGet = new HttpGet("https://example.com");
			httpClient.execute(httpGet, response -> {
				System.out.println("Status: " + response.getCode());
				EntityUtils.consume(response.getEntity());
				return null;
			});

			// Cookies are now stored in the cookieStore
			cookieStore.getCookies().forEach(System.out::println);
		}
	}

	/**
	 * Demonstrates an HTTP POST request with a JSON body.
	 */
	public static void httpPostJson() throws Exception {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost("https://api.example.com/users");
			String json = "{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"}";
			StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);

			httpClient.execute(httpPost, response -> {
				System.out.println("Status: " + response.getCode());
				System.out.println(EntityUtils.toString(response.getEntity()));
				return null;
			});
		}
	}

	/**
	 * Demonstrates a multipart file upload.
	 */
	public static void multipartFileUpload() throws Exception {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost("https://api.example.com/upload");
			File file = new File("my-file.txt");
			HttpEntity entity = MultipartEntityBuilder.create()
				.addBinaryBody("file", file, ContentType.APPLICATION_OCTET_STREAM, file.getName())
				.build();
			httpPost.setEntity(entity);

			httpClient.execute(httpPost, response -> {
				System.out.println("Status: " + response.getCode());
				EntityUtils.consume(response.getEntity());
				return null;
			});
		}
	}
}

