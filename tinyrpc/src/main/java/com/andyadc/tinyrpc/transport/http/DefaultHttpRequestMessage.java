package com.andyadc.tinyrpc.transport.http;

/**
 * 默认http请求消息
 */
public class DefaultHttpRequestMessage implements HttpRequestMessage {

	/**
	 * uri
	 */
	protected String uri;
	/**
	 * 方法
	 */
	protected HttpMethod httpMethod;
	/**
	 * 头部
	 */
	protected HttpHeaders httpHeaders;
	/**
	 * 内容
	 */
	protected byte[] content;

	public DefaultHttpRequestMessage() {
		content = new byte[0];
	}

	public DefaultHttpRequestMessage(String uri, HttpMethod httpMethod, HttpHeaders httpHeaders, byte[] content) {
		this.uri = uri;
		this.httpMethod = httpMethod;
		this.httpHeaders = httpHeaders;
		this.content = content == null ? new byte[0] : content;
	}

	@Override
	public String getUri() {
		return uri;
	}

	@Override
	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	@Override
	public byte[] content() {
		return content;
	}

	@Override
	public HttpHeaders headers() {
		return httpHeaders;
	}
}
