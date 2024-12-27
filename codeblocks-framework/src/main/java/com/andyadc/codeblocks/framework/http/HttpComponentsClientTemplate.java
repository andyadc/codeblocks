package com.andyadc.codeblocks.framework.http;

import com.andyadc.codeblocks.kit.collection.MapUtil;
import com.andyadc.codeblocks.kit.text.StringUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 1.基于HttpClient 4.5 +
 * 2.仅实现同步调用
 * 3.通用客户端，仅支持Rest调用，即请求和响应均为文本类型
 * <p>
 */
public class HttpComponentsClientTemplate extends AbstractHttpClientTemplate {

	private static final Logger logger = LoggerFactory.getLogger(HttpComponentsClientTemplate.class);

	private CloseableHttpClient httpClient;
	private ResponseHandler<String> responseHandler;

	private volatile boolean init = false;

	private List<HttpRequestInterceptor> requestInterceptors;

	private List<HttpResponseInterceptor> responseInterceptors;

	public HttpComponentsClientTemplate() {
		super();
	}

	public HttpComponentsClientTemplate(HttpClientConfiguration configuration) {
		super(configuration);
	}

	@Override
	public synchronized void init() {
		long t1 = System.nanoTime();
		if (init) {
			return;
		}
		super.init();

		// Avoid creating a new HttpClient instance for every request. Instead, reuse a single instance for better performance.
		httpClient = HttpComponentsClientBuilder.build(configuration, requestInterceptors, responseInterceptors);
		init = true;

		responseHandler = new BasicResponseHandler();

		long t2 = System.nanoTime();
		logger.info(String.format("HttpComponentsClient init elapsed time %.1fms", (t2 - t1) / 1e6d));
	}

	@Override
	public String get(String url) {
		return get(url, null);
	}

	@Override
	public String get(String url, Map<String, String> parameters) {
		return get(url, parameters, null);
	}

	@Override
	public String get(String url, Map<String, String> parameters, Map<String, String> headers) {
		if (StringUtil.isBlank(url)) {
			return null;
		}

		RequestBuilder requestBuilder = RequestBuilder.get(url);
		parameters(requestBuilder, parameters);
		//对于get请求，增补一个常规header
		String contentType = MessageFormat.format(CONTENT_TYPE_JSON_PATTERN, charset);
		requestBuilder.addHeader(HttpHeaders.CONTENT_TYPE, contentType);
		//用户自定义header（可以覆盖增补的header）
		headers(requestBuilder, headers);

		requestBuilder.setCharset(charset);
		HttpUriRequest request = requestBuilder.build();
		try {
			return process(request);
		} catch (Exception e) {
			throw new HttpRequestException("HttpComponentsClient error", e);
		}
	}

	@Override
	public String post(String url) {
		return post(url, null);
	}

	@Override
	public String post(String url, String content) {
		return post(url, content, null);
	}

	@Override
	public String post(String url, String content, Map<String, String> parameters) {
		return post(url, content, parameters, null);
	}

	@Override
	public String post(String url, String content, Map<String, String> parameters, Map<String, String> headers) {
		if (StringUtil.isBlank(url)) {
			return null;
		}

		RequestBuilder requestBuilder = RequestBuilder.post(url);
		parameters(requestBuilder, parameters);
		requestBuilder.setCharset(charset);
		if (content != null) {
			StringEntity stringEntity = new StringEntity(content, ContentType.create(CONTENT_TYPE_JSON, charset));
			requestBuilder.setEntity(stringEntity);
		} else {
			String contentType = MessageFormat.format(CONTENT_TYPE_JSON_PATTERN, charset.name());
			requestBuilder.addHeader(HttpHeaders.CONTENT_TYPE, contentType);
		}

		headers(requestBuilder, headers);

		HttpUriRequest request = requestBuilder.build();
		try {
			return process(request);
		} catch (Exception e) {
			throw new HttpRequestException("HttpComponentsClient error", e);
		}
	}

	@Override
	public String form(String url, Map<String, String> parameters) {
		return this.form(url, parameters, null);
	}

	@Override
	public String form(String url, Map<String, String> parameters, Map<String, String> headers) {
		if (StringUtil.isBlank(url)) {
			return null;
		}

		RequestBuilder requestBuilder = RequestBuilder.post(url);
		form(requestBuilder, parameters);
		requestBuilder.setCharset(charset);

		headers(requestBuilder, headers);

		HttpUriRequest request = requestBuilder.build();
		try {
			return process(request);
		} catch (Exception e) {
			throw new HttpRequestException("HttpComponentsClient error", e);
		}
	}

	/**
	 * TODO
	 */
	private String processWithResponseHandler(HttpUriRequest request) throws IOException {
		return httpClient.execute(request, responseHandler);
	}

	private String process(HttpUriRequest request) throws Exception {
		if (request == null) {
			throw new IllegalArgumentException("Request cannot be null");
		}

		try (CloseableHttpResponse response = httpClient.execute(request)) {
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode != HttpStatus.SC_OK) { // 200
				request.abort();
				throw new HttpRequestException(String.format(
					"HttpClient HTTP request failed with code: %d, reason: %s",
					statusCode,
					statusLine.getReasonPhrase()
				));
			}

			HttpEntity entity = response.getEntity();
			if (entity == null) {
				return null;
			}

			try {
				// Avoid EntityUtils.toString for Large Responses
				// Use InputStream for streaming large responses to avoid high memory usage.
				// <code> try (InputStream inputStream = entity.getContent()) { // Process the stream } </code>
				return EntityUtils.toString(entity, charset);
			} finally {
				// TODO after call toString() Should call consumeQuietly()
				EntityUtils.consumeQuietly(entity);
			}

		}
	}

	private void form(RequestBuilder requestBuilder, Map<String, String> parameters) {
		if (parameters == null || parameters.isEmpty()) {
			return;
		}
		List<NameValuePair> pairList = new ArrayList<>(parameters.size());
		parameters.forEach((k, v) -> pairList.add(new BasicNameValuePair(k, v)));
		UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(pairList, charset);
		requestBuilder.setEntity(formEntity);
	}

	private void parameters(RequestBuilder requestBuilder, Map<String, String> parameters) {
		if (parameters == null || parameters.isEmpty()) {
			return;
		}
		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			String value = entry.getValue() == null ? "" : entry.getValue();
			requestBuilder.addParameters(new BasicNameValuePair(entry.getKey(), value));
		}
	}

	private void headers(RequestBuilder requestBuilder, Map<String, String> headers) {
		if (requestBuilder == null) {
			return;
		}
		if (headers == null && globalHeaders == null) {
			return;
		}

		Map<String, String> _headers = new HashMap<>();
		if (MapUtil.isNotEmpty(globalHeaders)) {
			_headers.putAll(globalHeaders);
		}
		if (MapUtil.isNotEmpty(headers)) {
			_headers.putAll(headers);
		}

		_headers.forEach((key, value) -> {
			requestBuilder.addHeader(key, value != null ? value : "");
		});
	}

	@Override
	public HttpClient getClient() {
		return httpClient;
	}

	@Override
	public String clientType() {
		return HttpClientConfiguration.HTTP_CLIENT_TYPE_HTTP_COMPONENTS;
	}

	@Override
	public void close() throws IOException {
		if (httpClient != null) {
			httpClient.close();
		}
	}

	public void setRequestInterceptors(List<HttpRequestInterceptor> requestInterceptors) {
		this.requestInterceptors = requestInterceptors;
	}

	public void setResponseInterceptors(List<HttpResponseInterceptor> responseInterceptors) {
		this.responseInterceptors = responseInterceptors;
	}
}
