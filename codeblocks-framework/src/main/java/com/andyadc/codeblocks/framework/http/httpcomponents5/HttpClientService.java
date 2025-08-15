package com.andyadc.codeblocks.framework.http.httpcomponents5;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.ConnectTimeoutException;
import org.apache.hc.client5.http.HttpHostConnectException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.client5.http.ssl.TlsSocketStrategy;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.util.Timeout;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * What these various types of timeouts mean:
 *
 * <p>the Connection Timeout (http.connection.timeout) – the time to establish the connection with the remote host.</p>
 * <p>the Socket Timeout (http.socket.timeout) – the time waiting for data – after establishing the connection; maximum time of inactivity between two data packets.</p>
 * <p>the Connection Manager Timeout (http.connection-manager.timeout) – the time to wait for a connection from the connection manager/pool.</p>
 */
public final class HttpClientService {

	private static final int MAX_TOTAL_CONNECTIONS = 200;
	private static final int MAX_CONNECTIONS_PER_ROUTE = 20;
	private static final Timeout CONNECT_TIMEOUT = Timeout.ofSeconds(5);
	private static final Timeout SOCKET_TIMEOUT = Timeout.ofSeconds(5);
	private static final Timeout RESPONSE_TIMEOUT = Timeout.ofSeconds(10);
	private static final Timeout CONNECTION_REQUEST_TIMEOUT = Timeout.ofSeconds(5);

	private static volatile HttpClientService instance;
	private final CloseableHttpClient httpClient;
	private final ObjectMapper objectMapper = new ObjectMapper();

	private HttpClientService() {
		try {
			// Default request configuration
			RequestConfig defaultRequestConfig = RequestConfig.custom()
				.setResponseTimeout(RESPONSE_TIMEOUT)
				.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
				.build();

			// Default connection configuration
			ConnectionConfig connectionConfig = ConnectionConfig.custom()
				.setConnectTimeout(CONNECT_TIMEOUT)
				.setSocketTimeout(SOCKET_TIMEOUT)
				.build();

			final TlsSocketStrategy tlsStrategy = configureTlsStrategy();

			// Create a connection manager with a connection pool
			PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
				.setTlsSocketStrategy(tlsStrategy)
				.setMaxConnTotal(MAX_TOTAL_CONNECTIONS)
				.setMaxConnPerRoute(MAX_CONNECTIONS_PER_ROUTE)
				.setDefaultConnectionConfig(connectionConfig)
				.build();

			// Create the HttpClient
			this.httpClient = HttpClients.custom()
				.setConnectionManager(connectionManager)
				.setDefaultRequestConfig(defaultRequestConfig)
				.build();

		} catch (Exception e) {
			// Handle exceptions during initialization
			throw new RuntimeException("Failed to initialize HttpClientService", e);
		}
	}

	public static HttpClientService getInstance() {
		if (instance == null) {
			synchronized (HttpClientService.class) {
				if (instance == null) {
					instance = new HttpClientService();
				}
			}
		}
		return instance;
	}

	private static void printHeader(Header[] headers) {
		StringBuilder builder = new StringBuilder();
		for (Header header : headers) {
			builder.append(String.format("%s:%s, ", header.getName(), header.getValue()));
		}
		System.out.println(builder.toString());
	}

	// ------------------------------------------------------------------------------------------------------------------

	/**
	 * Create a registry of custom connection socket factories for supported
	 * protocol schemes.
	 */
	private TlsSocketStrategy configureTlsStrategy() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
		SSLContext sslContext = SSLContextBuilder.create()
			.loadTrustMaterial((X509Certificate[] chain, String authType) -> true) // 信任所有证书
			.build();

		return new DefaultClientTlsStrategy(sslContext);
	}

	/**
	 * Executes a GET request.
	 *
	 * @param url The URL to send the GET request to.
	 * @return The response body as a String.
	 * @throws IOException If an I/O error occurs.
	 */
	public String get(String url) throws IOException {
		final HttpGet httpGet = new HttpGet(url);

		// 定义 ResponseHandler
		HttpClientResponseHandler<String> responseHandler = (response) -> {
			final int statusCode = response.getCode();
			final HttpEntity entity = response.getEntity();
			String responseBody = (entity != null) ? EntityUtils.toString(entity) : null;

			if (statusCode < 200 || statusCode >= 300) {
				// 在 Handler 内部，我们仍然可以抛出自定义异常
				throw new HttpServiceException(
					"HTTP request failed",
					statusCode,
					responseBody
				);
			}

			return (responseBody == null) ? "" : responseBody;
		};

		try {
			// execute 方法会处理所有资源的关闭
			return httpClient.execute(httpGet, responseHandler);
		} catch (Exception e) {
			// 包装并重新抛出具体的网络/协议异常
			throw wrapException(url, e);
		}
	}

	/**
	 * Executes a POST request with a JSON body.
	 *
	 * @param url  The URL to send the POST request to.
	 * @param body The object to be serialized to JSON and sent as the request body.
	 * @return The response body as a String.
	 * @throws IOException If an I/O error occurs.
	 */
	public String postJson(String url, Object body) throws IOException {
		final HttpPost httpPost = new HttpPost(url);
		final String jsonBody = objectMapper.writeValueAsString(body);
		final StringEntity requestEntity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
		httpPost.setEntity(requestEntity);
		httpPost.setHeader(HttpHeaders.ACCEPT, "application/json");

		return httpClient.execute(httpPost, response -> {
			final HttpEntity entity = response.getEntity();
			String responseBody = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
			return responseBody;
		});
	}

	/**
	 * Executes a POST request with form-urlencoded data.
	 *
	 * @param url      The URL to send the POST request to.
	 * @param formData A map of form data.
	 * @return The response body as a String.
	 * @throws IOException If an I/O error occurs.
	 */
	public String postForm(String url, Map<String, String> formData) throws IOException {
		final HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<>();
		for (Map.Entry<String, String> entry : formData.entrySet()) {
			params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(params));

		return httpClient.execute(httpPost, response -> {
			printHeader(response.getHeaders());
			final HttpEntity entity = response.getEntity();
			String responseBody = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
			return responseBody;
		});
	}

	/**
	 * Executes a file upload using multipart/form-data.
	 *
	 * @param url           The URL to upload the file to.
	 * @param fileFieldName The name of the form field for the file.
	 * @param file          The file to upload.
	 * @param otherParams   Optional other text parameters to include in the form.
	 * @return The response body as a String.
	 * @throws IOException If an I/O error occurs.
	 */
	public String uploadFile(String url, String fileFieldName, File file, Map<String, String> otherParams, Map<String, String> headers) throws IOException {
		final HttpPost httpPost = new HttpPost(url);

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();

		//  Add the file part
		builder.addPart(fileFieldName, new FileBody(file, ContentType.DEFAULT_BINARY));

		// Add other text parts (if any)
		if (otherParams != null) {
			for (Map.Entry<String, String> entry : otherParams.entrySet()) {
				builder.addTextBody(entry.getKey(), entry.getValue());
			}
		}

		if (headers != null) {
			headers.forEach(httpPost::addHeader);
		}

		HttpEntity multipartEntity = builder.build();
		httpPost.setEntity(multipartEntity);

		return httpClient.execute(httpPost, response -> {
			final HttpEntity entity = response.getEntity();
			String responseBody = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
			return responseBody;
		});
	}

	/**
	 * Downloads a file from a URL and saves it to a destination path.
	 * This method streams the file directly to disk to handle large files efficiently.
	 *
	 * @param url             The URL to download the file from.
	 * @param destinationPath The path (including filename) to save the file to.
	 * @throws IOException If an I/O error occurs or the server returns a non-2xx status.
	 */
	public void downloadFile(String url, String destinationPath) throws IOException {
		final HttpGet httpGet = new HttpGet(url);

		// 使用 try-with-resources 来确保 response 总是被关闭
		try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
			final int statusCode = response.getCode();
			if (statusCode < 200 || statusCode >= 300) {
				throw new IOException("Failed to download file. Server responded with status code: " + statusCode);
			}

			final HttpEntity entity = response.getEntity();
			if (entity == null) {
				throw new IOException("Server response contained no content.");
			}

			// 确保目标目录存在
			Path outputPath = Paths.get(destinationPath);
			if (outputPath.getParent() != null) {
				outputPath.getParent().toFile().mkdirs();
			}

			// 使用 try-with-resources 来确保流被正确关闭
			try (InputStream inputStream = entity.getContent();
				 FileOutputStream fileOutputStream = new FileOutputStream(destinationPath)) {

				byte[] buffer = new byte[8192]; // 8K buffer
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					fileOutputStream.write(buffer, 0, bytesRead);
				}
			}
		}
	}

	/**
	 * Shuts down the HttpClient and releases all resources.
	 */
	public void shutdown() {
		try {
			if (this.httpClient != null)
				this.httpClient.close();
		} catch (IOException e) {
			// Log the exception, but we are shutting down anyway.
			System.err.println("Error shutting down HttpClient: " + e.getMessage());
		}
	}

	private IOException wrapException(String url, Exception e) {
		if (e instanceof ConnectTimeoutException) {
			return new IOException("Connection to " + url + " timed out.", e);
		}
		if (e instanceof SocketTimeoutException) {
			return new IOException("Socket timeout while communicating with " + url, e);
		}
		if (e instanceof UnknownHostException) {
			return new IOException("Unknown host: " + url, e);
		}
		if (e instanceof HttpHostConnectException) {
			return new IOException("Failed to connect to " + url, e);
		}
		if (e instanceof IOException) { // 如果已经是IOException, 直接抛出
			return (IOException) e;
		}
		// 对于其他检查型或运行时异常，包装成通用的IOException
		return new IOException("An unexpected error occurred for URL: " + url, e);
	}

}
