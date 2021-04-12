package com.andyadc.codeblocks.framework.http;

import com.andyadc.codeblocks.kit.collection.MapUtil;
import com.andyadc.codeblocks.kit.text.StringUtil;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * andaicheng
 * 2019-12-07
 */
public class OkHttpClientTemplate extends AbstractHttpClientTemplate {

	private static final Logger logger = LoggerFactory.getLogger(OkHttpClientTemplate.class);

	private OkHttpClient httpClient;
	private volatile boolean init = false;
	private List<Interceptor> interceptors;

	public OkHttpClientTemplate() {
		super();
	}

	public OkHttpClientTemplate(HttpClientConfiguration configuration) {
		super(configuration);
	}

	@Override
	public synchronized void init() {
		if (init) {
			return;
		}
		Instant begin = Instant.now();
		super.init();
		httpClient = OkHttpClientBuilder.build(configuration(), interceptors);
		init = true;
		logger.info("OkHttpClient init elapsed time: {}", Duration.between(begin, Instant.now()).toMillis());
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

		Request.Builder builder = new Request.Builder();
		try {
			builder.url(url(uri, parameters));
			builder.header("Content-type", MessageFormat.format(CONTENT_TYPE_JSON_PATTERN, charset));
			headers(builder, headers);
			Request request = builder.get().build();
			return process(request);
		} catch (Exception e) {
			throw new RuntimeException("OkHttpClient error.", e);
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

		Request.Builder builder = new Request.Builder();
		try {
			builder.url(url(uri, parameters));
			headers(builder, headers);
			String contentType = MessageFormat.format(CONTENT_TYPE_JSON_PATTERN, charset);
			RequestBody body = RequestBody.create(content, MediaType.parse(contentType));
			builder.post(body);

			Request request = builder.build();
			return process(request);
		} catch (Exception e) {
			throw new RuntimeException("OkHttpClient error", e);
		}
	}

	@Override
	public OkHttpClient getClient() {
		return httpClient;
	}

	@Override
	public String clientType() {
		return HttpClientConfiguration.HTTP_CLIENT_TYPE_OK_HTTP;
	}

	@Override
	public void close() {
		if (httpClient != null) {
			httpClient.dispatcher().executorService().shutdown();
		}
	}

	private String process(Request request) throws Exception {
		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new RuntimeException("OkHttpClient unexpected code " + response);
			}
			ResponseBody body = response.body();
			String result = null;
			if (body != null) {
				result = body.string();
			}
			return result;
		}
	}

	private String url(String uri, Map<String, String> parameters) throws Exception {
		if (parameters == null || parameters.isEmpty()) {
			return uri;
		}

		StringBuilder builder = new StringBuilder(uri);
		if (!uri.contains("?")) {
			builder.append("?1=1");
		}
		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			String value = entry.getValue();
			String encodedValue = "";
			if (value != null) {
				encodedValue = URLEncoder.encode(entry.getValue(), charset);
			}
			builder.append("&").append(entry.getKey())
				.append("=")
				.append(encodedValue);
		}
		return builder.toString();
	}

	private void headers(Request.Builder builder, Map<String, String> headers) {
		if (headers == null && globalHeaders == null) {
			return;
		}

		Map<String, String> _headers = new HashMap<>();
		if (MapUtil.isNotEmpty(globalHeaders)) {
			_headers.putAll(globalHeaders);
		}
		if (MapUtil.isNotEmpty(headers)) {
			_headers.putAll(headers); // 允许自定义header覆盖全局
		}

		for (Map.Entry<String, String> entry : _headers.entrySet()) {
			String value = entry.getValue() == null ? "" : entry.getValue();
			builder.header(entry.getKey(), value);
		}
	}

	public void setInterceptors(List<Interceptor> interceptors) {
		this.interceptors = interceptors;
	}
}
