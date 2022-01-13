package com.andyadc.tinyrpc.transport.http;

import com.andyadc.tinyrpc.transport.DefaultHttpHeaders;

/**
 * 默认http应答消息
 */
public class DefaultHttpResponseMessage implements HttpResponseMessage {

	/**
	 * 头部
	 */
	protected HttpHeaders httpHeaders = new DefaultHttpHeaders();
	/**
	 * 状态
	 */
	protected int status;
	/**
	 * 内容
	 */
	protected byte[] content;

	@Override
	public void setContent(byte[] bytes) {
		this.content = bytes;
	}

	@Override
	public int getStatus() {
		return status;
	}

	@Override
	public void setStatus(int status) {
		this.status = status;
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
