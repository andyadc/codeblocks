package com.andyadc.codeblocks.framework.http;

import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * andy.an
 * 2019/12/6
 */
public final class HttpComonentsClientBuilder {

	public static CloseableHttpClient build(HttpClientConfiguration configuration) {
		return build(configuration, null, null);
	}

	public static CloseableHttpClient build(HttpClientConfiguration configuration,
											List<HttpRequestInterceptor> requestInterceptors,
											List<HttpResponseInterceptor> responseInterceptors) {
		HttpClientBuilder builder = HttpClientBuilder.create();

		DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(configuration.getRetryTimes(), configuration.getRetryOnFailure());
		builder.setRetryHandler(retryHandler);
		builder.setConnectionTimeToLive(configuration.getKeepAliveTime(), TimeUnit.MILLISECONDS);
		if (requestInterceptors != null && !requestInterceptors.isEmpty()) {
			for (HttpRequestInterceptor interceptor : requestInterceptors) {
				builder.addInterceptorLast(interceptor);
			}
		}

		if (responseInterceptors != null && !responseInterceptors.isEmpty()) {
			for (HttpResponseInterceptor interceptor : responseInterceptors) {
				builder.addInterceptorLast(interceptor);
			}
		}

		//connectionRequestTimeout:
		//连接池模式时，从pool中获取连接的阻塞最大时长。默认是连接池，默认永久阻塞。
		//RT最大为1.5S
		//连接数最大为1024
		//每个域名最多连接数64
		//如果上述值，需要修改，请用户自己手动创建客户端，
		// 言外之意，这些参数是默认规范
		RequestConfig defaultRequestConfig = RequestConfig.custom()
			.setConnectTimeout(configuration.getConnectionTimeout().intValue())
			.setSocketTimeout(configuration.getSocketTimeout().intValue())
			.setConnectionRequestTimeout(configuration.getConnectionRequestTimeout())//请求最大时长1.5S,
			.build();
		builder.setDefaultRequestConfig(defaultRequestConfig);
		builder.setMaxConnTotal(configuration.getMaxConnections());
		builder.setMaxConnPerRoute(128);//常量值
		builder.evictExpiredConnections();//过期移除
		builder.evictIdleConnections(10, TimeUnit.SECONDS);//空闲10秒移除

		return builder.build();
	}

	/**
	 * 基于常规默认
	 */
	public static CloseableHttpClient build() {
		HttpClientConfiguration configuration = HttpClientConfiguration.common();
		return build(configuration);
	}
}
