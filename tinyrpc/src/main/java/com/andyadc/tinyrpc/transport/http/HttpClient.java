package com.andyadc.tinyrpc.transport.http;

import java.io.IOException;

/**
 * http 客户端
 */
public interface HttpClient {

	/**
	 * 执行 HTTP 请求
	 *
	 * @param request 请求
	 * @return 应答
	 * @throws IOException exception
	 */
	HttpResponse execute(HttpRequest request) throws IOException;
}
