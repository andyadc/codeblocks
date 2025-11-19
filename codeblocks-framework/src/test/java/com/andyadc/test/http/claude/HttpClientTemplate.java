package com.andyadc.test.http.claude;

import com.andyadc.codeblocks.framework.http.HttpRequestException;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientTemplate {

	private static final Logger logger = LoggerFactory.getLogger(HttpClientTemplate.class);

	private final CloseableHttpClient httpClient;
	private final PoolingHttpClientConnectionManager connectionManager;
	private final IdleConnectionMonitor idleConnectionMonitor;
	private final ConnectionPoolMetrics metrics;

	public HttpClientTemplate() {
		this.connectionManager = createConnectionManager();
		this.httpClient = createHttpClient(connectionManager);
		this.idleConnectionMonitor = new IdleConnectionMonitor(connectionManager);
		this.idleConnectionMonitor.start();
		this.metrics = new ConnectionPoolMetrics(connectionManager);
		this.metrics.startMonitoring();

		logger.info("HttpClientTemplate initialized.");
	}

	public String fecth(String url) throws IOException {

		ResponseHandler<String> responseHandler = response -> {
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				// Successfully converts entity to String and auto-closes stream
				return response.getEntity() != null ? EntityUtils.toString(response.getEntity()) : null;
			} else {
				throw new HttpRequestException("Unexpected response status: " + status);
			}
		};

		HttpGet httpGet = new HttpGet(url);
		return httpClient.execute(httpGet, responseHandler);
	}

	public void get(String url, Map<String, String> headers) {
		HttpGet httpGet = new HttpGet(url);

		addHeaders(httpGet, headers);

		HttpRequestResult result = executeRequest(httpGet);
		System.out.println(result);
	}

	/**
	 * Add headers to request
	 */
	private void addHeaders(HttpRequest request, Map<String, String> headers) {
		if (headers != null) {
			headers.forEach(request::addHeader);
		}
	}

	public void form(String url, Map<String, String> parameters, Map<String, String> headers) {
		RequestBuilder requestBuilder = RequestBuilder.post(url);

		if (parameters != null) {
			List<NameValuePair> pairList = new ArrayList<>(parameters.size());
			parameters.forEach((k, v) -> pairList.add(new BasicNameValuePair(k, v)));
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(pairList, StandardCharsets.UTF_8);
			requestBuilder.setEntity(formEntity);
		}

		if (headers != null) {
			headers.forEach((key, value) -> requestBuilder.addHeader(key, value != null ? value : ""));
		}

		HttpUriRequest request = requestBuilder.build();
		HttpRequestResult result = executeRequest(request);

	}

	private HttpRequestResult executeRequest(HttpUriRequest request) {
		long startTime = System.currentTimeMillis();

		try (CloseableHttpResponse response = httpClient.execute(request)) {
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			String responseBody = EntityUtils.toString(response.getEntity());

			long duration = System.currentTimeMillis() - startTime;
			logger.info("Request completed in {}ms with status {}", duration, statusCode);

			return new HttpRequestResult(statusCode, responseBody, true, null);
		} catch (IOException e) {
			logger.error("Error executing request", e);
			return HttpRequestResult.error(e.getMessage());
		}
	}

	private PoolingHttpClientConnectionManager createConnectionManager() {

		SSLConnectionSocketFactory sslConnectionSocketFactory = createInsecureSSL();

		Registry<ConnectionSocketFactory> socketFactoryRegistry =
			RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", sslConnectionSocketFactory)
				.build();

		PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager(
			socketFactoryRegistry
		);
		manager.setMaxTotal(200);
		manager.setDefaultMaxPerRoute(20);

		// Configure socket settings
		SocketConfig socketConfig = SocketConfig.custom()
			.setSoTimeout(30_000)
			.setTcpNoDelay(true)
			.setSoKeepAlive(true)
			.build();
		manager.setDefaultSocketConfig(socketConfig);

		return manager;
	}

	private SSLConnectionSocketFactory createSecureSSL() {
		SSLContext sslContext;
		String truststorePath = ""; // todo
		String truststorePassword = ""; // todo
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType()); // jks
			try (FileInputStream instream = new FileInputStream(truststorePath)) {
				trustStore.load(instream, truststorePassword.toCharArray());
			}

			sslContext = SSLContexts.custom()
				.loadTrustMaterial(trustStore, null)
				.build();

			return new SSLConnectionSocketFactory(
				sslContext,
				new String[]{"TLSv1.2", "TLSv1.3"}, // allowed protocols (TLSv1.3 requires a supporting JDK/OS)
				null,
				SSLConnectionSocketFactory.getDefaultHostnameVerifier()
			);
		} catch (Exception e) {
			throw new RuntimeException("Failed to create SSL context", e);
		}
	}

	/**
	 * Trust all certificates (NOT for prod)
	 */
	private SSLConnectionSocketFactory createInsecureSSL() {
		SSLContext sslContext;
		try {
			sslContext = SSLContextBuilder.create()
				.loadTrustMaterial(new TrustSelfSignedStrategy())
				.build();
			return new SSLConnectionSocketFactory(
				sslContext,
				NoopHostnameVerifier.INSTANCE);
		} catch (Exception e) {
			throw new RuntimeException("Failed to create SSL context", e);
		}
	}

	private CloseableHttpClient createHttpClient(PoolingHttpClientConnectionManager connectionManager) {
		RequestConfig requestConfig = RequestConfig.custom()
			.setConnectTimeout(5000)
			.setSocketTimeout(30000)
			.setConnectionRequestTimeout(5000)
			.build();

		return HttpClients.custom()
			.setConnectionManager(connectionManager)
			.setDefaultRequestConfig(requestConfig)
			.setRetryHandler(new CustomRetryHandler())
			.setServiceUnavailableRetryStrategy(new CustomServiceUnavailableRetryStrategy())
			.addInterceptorFirst(new LoggingInterceptors.RequestLoggingInterceptor())
			.addInterceptorLast(new LoggingInterceptors.ResponseLoggingInterceptor())
			.build();
	}

	public void shutdown() {
		logger.info("Shutting down HttpClientTemplate");

		try {
			metrics.stopMonitoring();
			idleConnectionMonitor.shutdown();
			httpClient.close();
			connectionManager.close();
			logger.info("HttpClientTemplate shutdown complete");
		} catch (Exception e) {
			logger.error("Error during shutdown", e);
		}
	}

	/**
	 * Get connection pool statistics
	 */
	public String getPoolStats() {
		return connectionManager.getTotalStats().toString();
	}

}
