package com.andyadc.codeblocks.framework.http;

import com.andyadc.codeblocks.kit.concurrent.ThreadUtil;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.impl.io.DefaultHttpResponseParserFactory;
import org.apache.http.pool.PoolStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

public final class HttpComponentsClientBuilder {

	public static CloseableHttpClient build(HttpClientConfiguration configuration) {
		return build(configuration, null, null);
	}

	public static CloseableHttpClient build(HttpClientConfiguration configuration,
											List<HttpRequestInterceptor> requestInterceptors,
											List<HttpResponseInterceptor> responseInterceptors) {
		HttpClientBuilder builder = HttpClientBuilder.create();

		if (requestInterceptors != null && !requestInterceptors.isEmpty()) {
			requestInterceptors.forEach(builder::addInterceptorFirst);
		}
		if (responseInterceptors != null && !responseInterceptors.isEmpty()) {
			responseInterceptors.forEach(builder::addInterceptorLast);
		}

		PoolingHttpClientConnectionManager defaultManager = defaultPoolingHttpClientConnectionManager(configuration);
		builder.setConnectionManagerShared(false);  // 连接池是否共享模式
		builder.setConnectionManager(defaultManager);

		RequestConfig defaultRequestConfig = RequestConfig.custom()
			.setConnectTimeout(configuration.getConnectionTimeout().intValue())
			.setSocketTimeout(configuration.getSocketTimeout().intValue())
			.setConnectionRequestTimeout(configuration.getConnectionRequestTimeout())//请求最大时长1.5S,
			.build();
		builder.setDefaultRequestConfig(defaultRequestConfig);

		// Set connection management settings
//		builder.setMaxConnTotal(configuration.getMaxConnections());
//		builder.setMaxConnPerRoute(128);
		builder.setConnectionTimeToLive(configuration.getKeepAliveTime(), TimeUnit.MILLISECONDS);
		builder.evictExpiredConnections(); // 过期移除
		builder.evictIdleConnections(10, TimeUnit.SECONDS); // 空闲10秒移除

		DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(configuration.getRetryTimes(), configuration.getRetryOnFailure());
		builder.setRetryHandler(retryHandler);

//		builder.disableAutomaticRetries();

//		builder.setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE);  // 默认长连接策略
		builder.setKeepAliveStrategy(new CustomConnectionKeepAliveStrategy());  // 自定义长连接策略
		builder.setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE);// 连接重用策略

		StatsMonitorThread statsMonitorThread = new StatsMonitorThread(defaultManager);
		statsMonitorThread.start();

		return builder.build();
	}

	private static PoolingHttpClientConnectionManager defaultPoolingHttpClientConnectionManager(HttpClientConfiguration configuration) {
		//HttpConnection 工厂; 配置写请求/解析响应处理器
		HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connectionFactory = new ManagedHttpClientConnectionFactory(
			DefaultHttpRequestWriterFactory.INSTANCE,
			DefaultHttpResponseParserFactory.INSTANCE
		);

		//注册访问协议相关的Socket工厂
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
			.register("http", PlainConnectionSocketFactory.INSTANCE)
			.register("https", SSLConnectionSocketFactory.getSystemSocketFactory())
			.build();

		//DNS 解析器
		DnsResolver dnsResolver = SystemDefaultDnsResolver.INSTANCE;

		//创建池化连接管理器
		PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager(
			socketFactoryRegistry,
			connectionFactory,
			dnsResolver
		);

		SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
		manager.setDefaultSocketConfig(socketConfig);
		//每个路由的默认最大连接, 每个路由实际最大连接数默认为
		//DefaultMaxPerRoute控制, 而MaxTotal是控制整个池子最大数
		//设置过小无法支持大并发(ConnectionPoolTimeoutException: Timeout waiting for connection from pool)
		//路由是对MaxTotal的细分
		manager.setMaxTotal(configuration.getMaxConnections()); // 设置整个连接池的最大连接数
		manager.setDefaultMaxPerRoute(128);// 每个路由最大连接数
		// 从连接池获取连接时, 链接不活跃多长时间需要进行一次验证, 默认2s
		manager.setValidateAfterInactivity(5 * 1000);

		return manager;
	}

	/**
	 * 基于常规默认
	 */
	public static CloseableHttpClient build() {
		HttpClientConfiguration configuration = HttpClientConfiguration.common();
		return build(configuration);
	}

	// Thread to monitor
	private static class StatsMonitorThread extends Thread {

		private static final Logger logger = LoggerFactory.getLogger(StatsMonitorThread.class);

		private final PoolingHttpClientConnectionManager connectionManager;

		public StatsMonitorThread(PoolingHttpClientConnectionManager connectionManager) {
			super("Stats Monitor Thread");
			this.connectionManager = connectionManager;
		}

		@Override
		public void run() {
			while (true) {
				ThreadUtil.sleep(3000L);
				logPoolStats(connectionManager);
			}
		}

		private void logPoolStats(PoolingHttpClientConnectionManager connectionManager) {
			PoolStats stats = connectionManager.getTotalStats();
			String statsConnInfo = String.format("Available: %d, Leased: %d, Pending: %d, Max: %d%n",
				stats.getAvailable(),  // Free connections in pool
				stats.getLeased(),     // Currently in use
				stats.getPending(),     // Connection requests waiting
				stats.getMax()         // Maximum allowed connections
			);
			logger.info("{}", statsConnInfo);
		}
	}
}
