package com.andyadc.tinyrpc.transport.http;

/**
 * http消息
 */
public interface HttpMessage {

	HttpHeaders headers();

	//TODO 为啥使用 Buffer 来进行读取
	byte[] content();
}
