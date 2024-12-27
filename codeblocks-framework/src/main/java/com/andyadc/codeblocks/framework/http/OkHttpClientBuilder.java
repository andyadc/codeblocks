package com.andyadc.codeblocks.framework.http;

import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class OkHttpClientBuilder {

	public static OkHttpClient build(HttpClientConfiguration configuration) {
		return build(configuration, null);
	}

	public static OkHttpClient build(HttpClientConfiguration configuration, List<Interceptor> interceptors) {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();

		// 不能设置最大连接数，默认连接是可重用的
		ConnectionPool connectionPool = new ConnectionPool(3, configuration.getKeepAliveTime(), TimeUnit.MILLISECONDS);
		builder.connectionPool(connectionPool);

		builder.connectTimeout(configuration.getConnectionTimeout(), TimeUnit.MILLISECONDS);
		builder.readTimeout(configuration.getSocketTimeout(), TimeUnit.MILLISECONDS);
		builder.writeTimeout(configuration.getSocketTimeout(), TimeUnit.MILLISECONDS);
		builder.retryOnConnectionFailure(configuration.getRetryOnFailure());

		if (interceptors != null && !interceptors.isEmpty()) {
			interceptors.forEach(builder::addInterceptor);
		}

		return builder.build();
	}

	public static OkHttpClient build() {
		HttpClientConfiguration configuration = HttpClientConfiguration.common();
		return build(configuration);
	}
}
