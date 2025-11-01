package com.andyadc.test.http.claude;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpClientUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	private static final int DEFAULT_CONNECT_TIMEOUT = 5000;
	private static final int DEFAULT_SOCKET_TIMEOUT = 30000;
	private static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 3000;
	private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 200;
	private static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 50;

	private final CloseableHttpClient httpClient;
	private final PoolingHttpClientConnectionManager connectionManager;

	public HttpClientUtil() {
		this(new HttpClientConfig());
	}

	public HttpClientUtil(HttpClientConfig config) {
		this.connectionManager = createConnectionManager(config);
		this.httpClient = createHttpClient(config);

		// 启动连接清理线程
		startConnectionCleanupThread();
	}

	/**
	 * 创建连接管理器
	 */
	private PoolingHttpClientConnectionManager createConnectionManager(HttpClientConfig config) {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(config.getMaxTotalConnections());
		cm.setDefaultMaxPerRoute(config.getMaxConnectionsPerRoute());

		// 设置SSL上下文（如果需要）
		if (config.isIgnoreSSL()) {
			try {
				SSLContext sslContext = SSLContextBuilder.create()
					.loadTrustMaterial(new TrustSelfSignedStrategy())
					.build();
				SSLConnectionSocketFactory sslSocketFactory =
					new SSLConnectionSocketFactory(sslContext);
				cm.setSSLSocketFactory(sslSocketFactory);

			} catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
				logger.warn("Failed to create SSL context", e);
			}
		}

		return cm;
	}

	/**
	 * 创建HttpClient实例
	 */
	private CloseableHttpClient createHttpClient(HttpClientConfig config) {
		RequestConfig requestConfig = RequestConfig.custom()
			.setConnectTimeout(config.getConnectTimeout())
			.setSocketTimeout(config.getSocketTimeout())
			.setConnectionRequestTimeout(config.getConnectionRequestTimeout())
			.build();

		HttpClientBuilder builder = HttpClients.custom()
			.setConnectionManager(connectionManager)
			.setDefaultRequestConfig(requestConfig);

		// 添加重试处理器
		if (config.getRetryCount() > 0) {
			builder.setRetryHandler(new CustomHttpRequestRetryHandler(config.getRetryCount()));
		}

		return builder.build();
	}

	/**
	 * 启动连接清理线程
	 */
	private void startConnectionCleanupThread() {
		Thread cleanupThread = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					Thread.sleep(30000); // 每30秒清理一次
					connectionManager.closeExpiredConnections();
					connectionManager.closeIdleConnections(60, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
		});
		cleanupThread.setDaemon(true);
		cleanupThread.setName("HttpClient-ConnectionCleanup");
		cleanupThread.start();
	}

	/**
	 * GET请求
	 */
	public HttpResult get(String url) {
		return get(url, null);
	}

	public HttpResult get(String url, Map<String, String> headers) {
		HttpGet httpGet = new HttpGet(url);
		addHeaders(httpGet, headers);
		return executeRequest(httpGet);
	}

	/**
	 * POST请求 - JSON数据
	 */
	public HttpResult postJson(String url, String jsonData) {
		return postJson(url, jsonData, null);
	}

	public HttpResult postJson(String url, String jsonData, Map<String, String> headers) {
		HttpPost httpPost = new HttpPost(url);
		addHeaders(httpPost, headers);

		try {
			StringEntity entity = new StringEntity(jsonData, StandardCharsets.UTF_8);
			entity.setContentType("application/json");
			httpPost.setEntity(entity);
			return executeRequest(httpPost);
		} catch (Exception e) {
			logger.error("Error creating JSON entity", e);
			return HttpResult.error("Error creating JSON entity: " + e.getMessage());
		}
	}

	/**
	 * POST请求 - 表单数据
	 */
	public HttpResult postForm(String url, Map<String, String> formData) {
		return postForm(url, formData, null);
	}

	public HttpResult postForm(String url, Map<String, String> formData, Map<String, String> headers) {
		HttpPost httpPost = new HttpPost(url);
		addHeaders(httpPost, headers);

		try {
			List<NameValuePair> params = new ArrayList<>();
			if (formData != null) {
				formData.forEach((key, value) -> params.add(new BasicNameValuePair(key, value)));
			}

			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, StandardCharsets.UTF_8);
			httpPost.setEntity(entity);
			return executeRequest(httpPost);
		} catch (Exception e) {
			logger.error("Error creating form entity", e);
			return HttpResult.error("Error creating form entity: " + e.getMessage());
		}
	}

	/**
	 * PUT请求
	 */
	public HttpResult put(String url, String data, Map<String, String> headers) {
		HttpPut httpPut = new HttpPut(url);
		addHeaders(httpPut, headers);

		try {
			if (data != null) {
				StringEntity entity = new StringEntity(data, StandardCharsets.UTF_8);
				httpPut.setEntity(entity);
			}
			return executeRequest(httpPut);
		} catch (Exception e) {
			logger.error("Error creating PUT entity", e);
			return HttpResult.error("Error creating PUT entity: " + e.getMessage());
		}
	}

	/**
	 * DELETE请求
	 */
	public HttpResult delete(String url, Map<String, String> headers) {
		HttpDelete httpDelete = new HttpDelete(url);
		addHeaders(httpDelete, headers);
		return executeRequest(httpDelete);
	}

	/**
	 * 添加请求头
	 */
	private void addHeaders(HttpRequestBase request, Map<String, String> headers) {
		if (headers != null) {
			headers.forEach(request::addHeader);
		}
	}

	/**
	 * 执行HTTP请求
	 */
	private HttpResult executeRequest(HttpRequestBase request) {
		long startTime = System.currentTimeMillis();

		try (CloseableHttpResponse response = httpClient.execute(request)) {
			int statusCode = response.getStatusLine().getStatusCode();
			String responseBody = "";

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				responseBody = EntityUtils.toString(entity, StandardCharsets.UTF_8);
				EntityUtils.consume(entity);
			}

			long duration = System.currentTimeMillis() - startTime;
			logger.debug("HTTP {} {} completed in {}ms, status: {}",
				request.getMethod(), request.getURI(), duration, statusCode);

			return new HttpResult(statusCode, responseBody, true, null);

		} catch (IOException e) {
			long duration = System.currentTimeMillis() - startTime;
			logger.error("HTTP {} {} failed after {}ms",
				request.getMethod(), request.getURI(), duration, e);
			return HttpResult.error("Request failed: " + e.getMessage());
		} finally {
			request.releaseConnection();
		}
	}

	/**
	 * 关闭HttpClient和连接管理器
	 */
	public void close() {
		try {
			if (httpClient != null) {
				httpClient.close();
			}
			if (connectionManager != null) {
				connectionManager.close();
			}
		} catch (IOException e) {
			logger.error("Error closing HttpClient", e);
		}
	}

	/**
	 * 获取连接池统计信息
	 */
	public String getConnectionPoolStats() {
		if (connectionManager != null) {
			return String.format("Total: %d, Leased: %d, Pending: %d, Available: %d",
				connectionManager.getTotalStats().getMax(),
				connectionManager.getTotalStats().getLeased(),
				connectionManager.getTotalStats().getPending(),
				connectionManager.getTotalStats().getAvailable());
		}
		return "Connection manager not available";
	}
}
