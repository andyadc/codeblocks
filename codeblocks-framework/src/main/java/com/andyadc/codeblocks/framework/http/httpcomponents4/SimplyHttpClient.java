package com.andyadc.codeblocks.framework.http.httpcomponents4;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.pool.PoolStats;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * apache http v4
 */
public class SimplyHttpClient {

	private static final Logger logger = LoggerFactory.getLogger(SimplyHttpClient.class);

	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
	private static volatile SimplyHttpClient instance;
	private final CloseableHttpClient httpClient;

	public SimplyHttpClient() {
		long start = System.currentTimeMillis();
		HttpClientBuilder builder = HttpClientBuilder.create();

		// 配置SSL上下文 ---
		// 创建一个信任所有证书的SSLContext
		// !!! 警告: 这在生产环境中是不安全的, 仅用于测试或内部网络 !!!
		SSLContext sslContext = createTrustAllSslContext();
		// 创建SSL连接套接字工厂, 并禁用主机名验证
		// !!! 警告: NoopHostnameVerifier禁用了主机名验证, 同样不安全 !!!
		SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

		// 创建套接字工厂注册表 ---
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
			.register("http", PlainConnectionSocketFactory.getSocketFactory())
			.register("https", sslSocketFactory) // 为https注册我们的自定义工厂
			.build();

		// 创建默认的请求配置
		RequestConfig defaultRequestConfig = RequestConfig.custom()
			// 服务器返回数据(socket)超时时间, 单位毫秒
			.setSocketTimeout(10 * 1000)
			// 连接上服务器(握手成功)超时时间, 单位毫秒
			.setConnectTimeout(5 * 1000)
			// 从连接池中获取连接的超时时间, 单位毫秒
			.setConnectionRequestTimeout(1000)
			.build();
		builder.setDefaultRequestConfig(defaultRequestConfig);

		// 创建连接池管理器
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		// 设置最大连接数
		connectionManager.setMaxTotal(200);
		// 设置每个路由的基础连接数
		connectionManager.setDefaultMaxPerRoute(50);
		// 配置空闲连接驱逐策略 (可选)
		// 定期清理无效的连接
		// connectionManager.closeIdleConnections(30, TimeUnit.SECONDS);

		SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
		connectionManager.setDefaultSocketConfig(socketConfig);
		builder.setConnectionManager(connectionManager);

		// 如有需要, 可以设置 Keep-Alive 策略
		builder.setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE);

		builder.disableAutomaticRetries();
		this.httpClient = builder.build();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				logger.info("Shutdown initiated...");
				if (httpClient != null)
					httpClient.close();
				logger.info("Shutdown completed.");
			} catch (IOException e) {
				logger.error("Shutdown error: " + e.getMessage());
			}
		}));
		PoolStats totalStats = connectionManager.getTotalStats();
		logger.info("PoolStats {}", totalStats);
		logger.info("Initialization completed in {} ms", (System.currentTimeMillis() - start));
	}

	public static SimplyHttpClient getInstance() {
		if (instance == null) {
			synchronized (SimplyHttpClient.class) {
				if (instance == null) {
					instance = new SimplyHttpClient();
				}
			}
		}
		return instance;
	}

	private SSLContext createTrustSslContext(String keyStoreType, String keyPath, String password) {
		try {
			KeyStore keyStore = KeyStore.getInstance(keyStoreType);
			try (InputStream inputStream = new FileInputStream(keyPath)) {
				keyStore.load(inputStream, password.toCharArray());
			}
			return SSLContexts.custom().loadTrustMaterial(keyStore, new TrustSelfSignedStrategy()).build();
		} catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | KeyManagementException e) {
			throw new RuntimeException("Failed to create a trust SSL context. keyStoreType: " + keyStoreType + ", keyPath: " + keyPath, e);
		}
	}

	/**
	 * 创建一个信任所有证书的SSLContext
	 *
	 * @return SSLContext
	 */
	private SSLContext createTrustAllSslContext() {
		try {
			SSLContext sslContext = SSLContext.getInstance("TLS");
			TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			}};
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
			return sslContext;
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			// 在实际应用中, 应当记录日志并优雅地处理这个异常
			throw new RuntimeException("Failed to create a trust-all SSL context", e);
		}
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * 执行 GET 请求
	 *
	 * @param url 请求地址
	 * @return 响应内容的字符串
	 * @throws IOException exception
	 */
	public String doGet(String url) throws IOException, URISyntaxException {
		return doGet(url, null, null);
	}

	/**
	 * 执行带参数的 GET 请求
	 *
	 * @param url    请求地址
	 * @param params 请求参数
	 * @return 响应内容的字符串
	 * @throws IOException        exception
	 * @throws URISyntaxException exception
	 */
	public String doGet(String url, Map<String, String> params) throws IOException, URISyntaxException {
		return doGet(url, params, null);
	}

	/**
	 * 执行带参数, 自定义header的 GET 请求
	 *
	 * @param url     请求地址
	 * @param params  请求参数
	 * @param headers 自定义请求头
	 * @return 响应内容的字符串
	 * @throws IOException        exception
	 * @throws URISyntaxException exception
	 */
	public String doGet(String url, Map<String, String> params, Map<String, String> headers) throws IOException, URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder(url);
		if (params != null) {
			params.forEach(uriBuilder::addParameter);
		}
		HttpGet httpGet = new HttpGet(uriBuilder.build());
		applyHeaders(httpGet, headers);
		return executeRequest(httpGet);
	}

	/**
	 * 执行 POST 请求, 提交 JSON 数据
	 *
	 * @param url      请求地址
	 * @param jsonBody JSON 格式的请求体
	 * @return 响应内容的字符串
	 * @throws IOException exception
	 */
	public String doPostJson(String url, String jsonBody) throws IOException {
		return doPostJson(url, jsonBody, null);
	}

	/**
	 * 执行 POST 请求, 提交 JSON 数据, 携带自定义请求头
	 *
	 * @param url      请求地址
	 * @param jsonBody JSON 格式的请求体
	 * @param headers  自定义请求头
	 * @return 响应内容的字符串
	 * @throws IOException exception
	 */
	public String doPostJson(String url, String jsonBody, Map<String, String> headers) throws IOException {
		HttpPost httpPost = new HttpPost(url);
		applyHeaders(httpPost, headers);
		if (jsonBody != null && !jsonBody.isEmpty()) {
			StringEntity stringEntity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
			httpPost.setEntity(stringEntity);
		}
		return executeRequest(httpPost);
	}

	/**
	 * 执行 POST 请求, 提交表单数据 (application/x-www-form-urlencoded)
	 *
	 * @param url        请求地址
	 * @param formParams 表单参数
	 * @return 响应内容的字符串
	 * @throws IOException exception
	 */
	public String doPostForm(String url, Map<String, String> formParams) throws IOException {
		return doPostForm(url, formParams, null);
	}

	/**
	 * 执行 POST 请求, 提交表单数据 (application/x-www-form-urlencoded)
	 *
	 * @param url        请求地址
	 * @param formParams 表单参数
	 * @param headers    自定义请求头
	 * @return 响应内容的字符串
	 * @throws IOException exception
	 */
	public String doPostForm(String url, Map<String, String> formParams, Map<String, String> headers) throws IOException {
		HttpPost httpPost = new HttpPost(url);
		applyHeaders(httpPost, headers);
		if (formParams != null) {
			List<NameValuePair> pairList = new ArrayList<>(formParams.size());
			for (Map.Entry<String, String> entry : formParams.entrySet()) {
				pairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(pairList, DEFAULT_CHARSET));
		}
		return executeRequest(httpPost);
	}

	/**
	 * 执行 HTTP 请求的核心方法
	 *
	 * @param request request HttpRequestBase
	 * @return 响应内容的字符串
	 * @throws IOException exception
	 */
	private String executeRequest(HttpUriRequest request) throws IOException {
		String responseBody;
		try (CloseableHttpResponse response = httpClient.execute(request)) {
			Header[] responseAllHeaders = response.getAllHeaders();
			for (Header header : responseAllHeaders) {
				logger.info("[" + header.getName() + " - " + header.getValue() + "]");
			}
			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			responseBody = EntityUtils.toString(entity, DEFAULT_CHARSET);
			if (statusCode < 200 || statusCode >= 300) {
				throw new IOException("Unexpected response status: " + statusCode + ", body: " + responseBody);
			}
			EntityUtils.consumeQuietly(entity);
		}
		return responseBody;
	}

	private void applyHeaders(HttpRequestBase request, Map<String, String> headers) {
		if (headers != null && !headers.isEmpty()) {
			headers.forEach(request::setHeader);
		}
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	public void shutdown() {
		try {
			if (this.httpClient != null)
				this.httpClient.close();
		} catch (IOException e) {
			// Log the exception, but we are shutting down anyway.
			logger.error("Error shutting down HttpClient: {}", e.getMessage());
			System.err.println("Error shutting down HttpClient: " + e.getMessage());
		}
	}

}
