package com.andyadc.codeblocks.framework.http;


import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import java.util.concurrent.TimeUnit;

/**
 * TODO HttpComponents version 5
 */
public final class HttpComponentsClient5Builder {

	public static CloseableHttpClient build() {

		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(defaultPoolingHttpClientConnectionManager());

		RequestConfig requestConfig = RequestConfig.custom()
			.setConnectionRequestTimeout(Timeout.of(10L, TimeUnit.SECONDS))
			.setResponseTimeout(Timeout.of(10L, TimeUnit.SECONDS))
			.build();

		builder.setDefaultRequestConfig(requestConfig);

		// Connection management settings
		builder.evictExpiredConnections();
		builder.evictIdleConnections(TimeValue.of(10, TimeUnit.SECONDS));

		builder.setRetryStrategy(new DefaultHttpRequestRetryStrategy());

		return builder.build();
	}


	private static PoolingHttpClientConnectionManager defaultPoolingHttpClientConnectionManager() {
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
			.register("http", PlainConnectionSocketFactory.getSocketFactory())
			.register("https", SSLConnectionSocketFactory.getSocketFactory())
			.build();

//		SSLContexts sslContexts = SSLContexts.custom().build();

		PoolingHttpClientConnectionManager manager = PoolingHttpClientConnectionManagerBuilder.create()

			.build();

		manager.setMaxTotal(100);
		manager.setDefaultMaxPerRoute(10);

		return manager;
	}

}
