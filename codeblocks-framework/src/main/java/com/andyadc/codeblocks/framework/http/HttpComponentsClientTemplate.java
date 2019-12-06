package com.andyadc.codeblocks.framework.http;

import com.andyadc.codeblocks.kit.text.StringUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ref:https://www.iteye.com/blog/shift-alt-ctrl-2433670
 * <p>
 * 1.基于HttpClient 4.5 +
 * 2.仅实现同步调用
 * 3.通用客户端，仅支持Rest调用，即请求和响应均为文本类型
 * <p>
 * andy.an
 * 2019/12/6
 */
public class HttpComponentsClientTemplate extends AbstractHttpClientTemplate {

	private CloseableHttpClient httpClient;

	private volatile boolean init = false;

	private List<HttpRequestInterceptor> requestInterceptors;

	private List<HttpResponseInterceptor> responseInterceptors;

	public HttpComponentsClientTemplate() {
		super();
	}

	public HttpComponentsClientTemplate(HttpClientConfiguration configuration) {
		super(configuration);
	}

	public List<HttpRequestInterceptor> getRequestInterceptors() {
		return requestInterceptors;
	}

	public List<HttpResponseInterceptor> getResponseInterceptors() {
		return responseInterceptors;
	}

	@Override
	public synchronized void init() {
		if (init) {
			return;
		}
		super.init();
		httpClient = HttpComonentsClientBuilder.build(configuration, requestInterceptors, responseInterceptors);
		init = true;
	}

	@Override
	public String get(String uri) {
		return get(uri, null);
	}

	@Override
	public String get(String uri, Map<String, String> parameters) {
		return get(uri, parameters, null);
	}

	@Override
	public String get(String uri, Map<String, String> parameters, Map<String, String> headers) {
		if (StringUtil.isBlank(uri)) {
			return null;
		}

		RequestBuilder requestBuilder = RequestBuilder.get(uri);
		parameters(requestBuilder, parameters);
		//对于get请求，增补一个常规header
		String contentType = MessageFormat.format(CONTENT_TYPE_JSON_PATTERN, charset);
		requestBuilder.addHeader(HttpHeaders.CONTENT_TYPE, contentType);
		//用户自定义header（可以覆盖增补的header）
		headers(requestBuilder, headers);

		requestBuilder.setCharset(Charset.forName(charset));
		HttpUriRequest request = requestBuilder.build();
		try {
			return process(request);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String post(String uri) {
		return post(uri, null);
	}

	@Override
	public String post(String uri, String content) {
		return post(uri, content, null);
	}

	@Override
	public String post(String uri, String content, Map<String, String> parameters) {
		return post(uri, content, parameters, null);
	}

	@Override
	public String post(String uri, String content, Map<String, String> parameters, Map<String, String> headers) {
		if (StringUtil.isBlank(uri)) {
			return null;
		}

		RequestBuilder requestBuilder = RequestBuilder.post(uri);
		parameters(requestBuilder, parameters);
		requestBuilder.setCharset(Charset.forName(charset));
		if (content != null) {
			StringEntity stringEntity = new StringEntity(content, ContentType.create(CONTENT_TYPE_JSON, charset));
			requestBuilder.setEntity(stringEntity);
		} else {
			String contentType = MessageFormat.format(CONTENT_TYPE_JSON_PATTERN, charset);
			requestBuilder.addHeader(HttpHeaders.CONTENT_TYPE, contentType);
		}

		headers(requestBuilder, headers);

		HttpUriRequest request = requestBuilder.build();
		try {
			return process(request);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String process(HttpUriRequest request) throws Exception {
		try (CloseableHttpResponse response = httpClient.execute(request)) {
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode != 200) {
				request.abort();
				throw new RuntimeException("HttpClient error, status:" + statusCode + ", message:" + statusLine.getReasonPhrase());
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, charset);
			}
			return result;
		}
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
		if (headers == null && globalHeaders == null) {
			return;
		}
		Map<String, String> _headers = new HashMap<>();
		if (globalHeaders != null && !globalHeaders.isEmpty()) {
			_headers.putAll(globalHeaders);
		}
		if (headers != null && !headers.isEmpty()) {
			_headers.putAll(headers);
		}
		for (Map.Entry<String, String> entry : _headers.entrySet()) {
			String value = entry.getValue() == null ? "" : entry.getValue();
			requestBuilder.addHeader(entry.getKey(), value);
		}
	}

	@Override
	public HttpClient getClient() {
		return httpClient;
	}

	@Override
	public String clientType() {
		return configuration.getClientType();
	}

	@Override
	public void close() throws IOException {
		if (httpClient != null) {
			httpClient.close();
		}
	}
}
