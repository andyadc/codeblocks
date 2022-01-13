package com.andyadc.tinyrpc.transport.http;

/**
 * http应答消息
 */
public interface HttpResponseMessage extends HttpMessage {

	/**
	 * 获取状态
	 *
	 * @return 状态
	 */
	int getStatus();

	/**
	 * 设置状态
	 *
	 * @param status 状态
	 */
	void setStatus(int status);

	/**
	 * 设置内容
	 *
	 * @param bytes 内容
	 */
	void setContent(byte[] bytes);
}
