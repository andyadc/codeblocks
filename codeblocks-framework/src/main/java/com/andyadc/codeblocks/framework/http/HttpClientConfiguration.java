package com.andyadc.codeblocks.framework.http;

import com.andyadc.codeblocks.common.Constants;

import java.io.Serializable;
import java.util.Map;

/**
 * andy.an
 * 2019/12/6
 */
public class HttpClientConfiguration implements Serializable {

	//连接创建
	public static final long DEFAULT_CONNECTION_TIMEOUT = 500;
	//SO_TIMEOUT
	public static final long DEFAULT_SOCKET_TIMEOUT = 500;
	//连接池中的连接被保活的时长
	public static final long DEFAULT_KEEP_ALIVE_TIME = 6000;
	//请求异常（exception），重试的次数，默认为0，不重试
	public static final int DEFAULT_HTTP_RETRY_TIMES = 0;
	//是否重试
	public static final boolean DEFAULT_HTTP_RETRY_ON_FAILURE = false;
	public static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 1500;
	public static final String HTTP_CLIENT_TYPE_OK_HTTP = "OkHttp";
	public static final String HTTP_CLIENT_TYPE_HTTP_COMPONENTS = "HttpComponents";
	private static final int DEFAULT_MAX_CONNECTIONS = 1024;
	//连接创建
	private long connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
	//SO_TIMEOUT
	private long socketTimeout = DEFAULT_SOCKET_TIMEOUT;

	//连接池中的连接被保活的时长
	private long keepAliveTime = DEFAULT_KEEP_ALIVE_TIME;

	//请求异常（exception），重试的次数，默认为0，不重试
	private int retryTimes = DEFAULT_HTTP_RETRY_TIMES;
	//是否重试
	private boolean retryOnFailure = DEFAULT_HTTP_RETRY_ON_FAILURE;

	private String charset = Constants.DEFAULT_CHARSET;

	//全局headers，即所有请求都会添加
	private Map<String, String> globalHeaders;

	private int connectionRequestTimeout = 1500;

	private HttpClientConfiguration() {
	}

	public static HttpClientConfiguration common(Map<String, String> globalHeaders) {
		HttpClientConfiguration configuration = new HttpClientConfiguration();
		configuration.setCharset(Constants.DEFAULT_CHARSET);//UTF-8
		configuration.setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);//500
		configuration.setSocketTimeout(DEFAULT_SOCKET_TIMEOUT);//500
		configuration.setKeepAliveTime(DEFAULT_KEEP_ALIVE_TIME);//6000
		configuration.setRetryOnFailure(DEFAULT_HTTP_RETRY_ON_FAILURE);//FALSE
		configuration.setRetryTimes(DEFAULT_HTTP_RETRY_TIMES);//0
		configuration.setConnectionRequestTimeout(DEFAULT_CONNECTION_REQUEST_TIMEOUT);
		if (globalHeaders != null && !globalHeaders.isEmpty()) {
			configuration.setGlobalHeaders(globalHeaders);
		}
		return configuration;
	}

	public static HttpClientConfiguration common() {
		return common(null);
	}

	public Long getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(Long connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public Long getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(Long socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public Long getKeepAliveTime() {
		return keepAliveTime;
	}

	public void setKeepAliveTime(Long keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

	public Integer getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(Integer retryTimes) {
		this.retryTimes = retryTimes;
	}

	public Boolean getRetryOnFailure() {
		return retryOnFailure;
	}

	public void setRetryOnFailure(Boolean retryOnFailure) {
		this.retryOnFailure = retryOnFailure;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public Map<String, String> getGlobalHeaders() {
		return globalHeaders;
	}

	public void setGlobalHeaders(Map<String, String> globalHeaders) {
		this.globalHeaders = globalHeaders;
	}

	public int getMaxConnections() {
		return DEFAULT_MAX_CONNECTIONS;
	}

	public int getConnectionRequestTimeout() {
		return connectionRequestTimeout;
	}

	public void setConnectionRequestTimeout(int connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
	}
}
