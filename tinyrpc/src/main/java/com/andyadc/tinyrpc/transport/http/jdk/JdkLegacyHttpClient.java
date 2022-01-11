package com.andyadc.tinyrpc.transport.http.jdk;

import com.andyadc.tinyrpc.transport.http.HttpClient;
import com.andyadc.tinyrpc.transport.http.HttpRequest;
import com.andyadc.tinyrpc.transport.http.HttpResponse;

import java.io.Closeable;
import java.io.IOException;
import java.net.HttpURLConnection;

public class JdkLegacyHttpClient implements HttpClient {

	protected static final int BUFFER_SIZE = 1024;

	@Override
	public HttpResponse execute(HttpRequest request) throws IOException {
		return null;
	}

	/**
	 * 关闭链接
	 */
	protected void close(final HttpURLConnection connection) {
		if (connection != null) {
			connection.disconnect();
		}
	}

	/**
	 * 关闭
	 */
	protected void close(final Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
				// ignore
			}
		}
	}
}
