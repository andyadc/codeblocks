package com.andyadc.tinyrpc.transport.http;

/**
 * http请求消息
 */
public interface HttpRequestMessage extends HttpMessage {

	/**
	 * 获取uri
	 *
	 * @return uri
	 */
	String getUri();

	/**
	 * 获取方法
	 *
	 * @return 方法
	 */
	HttpMethod getHttpMethod();
}
