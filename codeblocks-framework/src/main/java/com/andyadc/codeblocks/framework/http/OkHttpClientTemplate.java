package com.andyadc.codeblocks.framework.http;

import com.andyadc.codeblocks.kit.collection.MapUtil;
import com.andyadc.codeblocks.kit.text.StringUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		long t1 = System.nanoTime();
		if (init) {
			return;
		}
		super.init();

		httpClient = OkHttpClientBuilder.build(configuration(), interceptors);
		init = true;

		long t2 = System.nanoTime();
		logger.info(String.format("OkHttpClient init elapsed time %.1fms", (t2 - t1) / 1e6d));
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

		Request.Builder builder = new Request.Builder();
		try {
			builder.url(url(url, parameters));
			builder.header("Content-Type", MessageFormat.format(CONTENT_TYPE_JSON_PATTERN, charset.name()));
			headers(builder, headers);
			Request request = builder.get().build();
			return process(request);
		} catch (Exception e) {
			throw new HttpRequestException("OkHttpClient request error.", e);
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

		Request.Builder builder = new Request.Builder();
		try {
			builder.url(url(url, parameters));
			headers(builder, headers);
			String contentType = MessageFormat.format(CONTENT_TYPE_JSON_PATTERN, charset);
			RequestBody body = RequestBody.create(content, MediaType.parse(contentType));
			builder.post(body);

			Request request = builder.build();
			return process(request);
		} catch (Exception e) {
			throw new HttpRequestException("OkHttpClient error", e);
		}
	}

	@Override
	public String form(String url, Map<String, String> parameters) {
		return this.form(url, parameters, null);
	}

	@Override
	public String form(String url, Map<String, String> parameters, Map<String, String> headers) {
		if (parameters == null || parameters.size() < 1) {
			return null;
		}
		FormBody.Builder formBuilder = new FormBody.Builder();
		parameters.forEach(formBuilder::add);
		RequestBody formBody = formBuilder.build();

		Request.Builder builder = new Request.Builder();
		builder.url(url);
		builder.post(formBody);

		headers(builder, headers);

		Request request = builder.build();
		try {
			return process(request);
		} catch (Exception e) {
			throw new HttpRequestException("OkHttpClient error", e);
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

	private String process(Request request) throws IOException {
		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new HttpRequestException(String.format("OkHttpClient HTTP request failed with code %d: %s [URL: %s]",
					response.code(),
					response.message(),
					request.url()));
			}
			ResponseBody body = response.body();
			return (body != null) ? body.string() : null;
		}
	}

	/**
	 * TODO
	 * Asynchronous request
	 */
	private void asyncProcess(Request request, HttpCallback callback) {

		httpClient.newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Call call, IOException e) {
				logger.error("Request async failed for URL: {}", call.request().url(), e);
				if (callback != null) {
					callback.onFailure(e);
				}
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				try (ResponseBody responseBody = response.body()) {
					if (!response.isSuccessful()) {
						String errorMsg = String.format("HTTP %d: %s", response.code(), response.message());
						throw new HttpRequestException(errorMsg);
					}
					String result = responseBody != null ? responseBody.string() : null;
					if (callback != null) {
						callback.onResponse(result);
					}
				}
			}
		});
	}

	private String url(String url, Map<String, String> parameters) {
		if (parameters == null || parameters.isEmpty()) {
			return url;
		}

		StringBuilder builder = new StringBuilder(url);
		builder.append(url.contains("?") ? "&" : "?");

		try {
			boolean first = true;
			for (Map.Entry<String, String> entry : parameters.entrySet()) {
				if (!first) {
					builder.append("&");
				}
				builder.append(URLEncoder.encode(entry.getKey(), charset.name()))
					.append("=")
					.append(URLEncoder.encode(entry.getValue() != null ? entry.getValue() : "", charset.name()));
				first = false;
			}
		} catch (Exception e) {
			String erroMsg = String.format("Failed to encode URL parameters. url: %s, parameters: %s", url, parameters);
			throw new HttpRequestException(erroMsg, e);
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

	interface HttpCallback {
		void onResponse(String response);

		void onFailure(Exception e);
	}
}
