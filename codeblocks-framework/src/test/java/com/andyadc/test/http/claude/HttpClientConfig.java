package com.andyadc.test.http.claude;

public class HttpClientConfig {

	private int connectTimeout = 5_000;
	private int socketTimeout = 30_000;
	private int connectionRequestTimeout = 3_000;
	private int maxTotalConnections = 200;
	private int maxConnectionsPerRoute = 50;
	private int retryCount = 3;

	private boolean ignoreSSL = false;
	private String truststorePath;
	private String trustPass;

	// 构造函数和getter/setter方法
	public HttpClientConfig() {
	}

	public HttpClientConfig(int connectTimeout, int socketTimeout, int maxTotalConnections) {
		this.connectTimeout = connectTimeout;
		this.socketTimeout = socketTimeout;
		this.maxTotalConnections = maxTotalConnections;
	}

	// getter和setter方法
	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public int getConnectionRequestTimeout() {
		return connectionRequestTimeout;
	}

	public void setConnectionRequestTimeout(int connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
	}

	public int getMaxTotalConnections() {
		return maxTotalConnections;
	}

	public void setMaxTotalConnections(int maxTotalConnections) {
		this.maxTotalConnections = maxTotalConnections;
	}

	public int getMaxConnectionsPerRoute() {
		return maxConnectionsPerRoute;
	}

	public void setMaxConnectionsPerRoute(int maxConnectionsPerRoute) {
		this.maxConnectionsPerRoute = maxConnectionsPerRoute;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public boolean isIgnoreSSL() {
		return ignoreSSL;
	}

	public void setIgnoreSSL(boolean ignoreSSL) {
		this.ignoreSSL = ignoreSSL;
	}

}
