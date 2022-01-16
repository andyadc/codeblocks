package com.andyadc.tinyrpc.transport.http.jdk;

import com.andyadc.tinyrpc.transport.http.DefaultHttpHeaders;
import com.andyadc.tinyrpc.transport.http.HttpClient;
import com.andyadc.tinyrpc.transport.http.HttpHeaders;
import com.andyadc.tinyrpc.transport.http.HttpRequest;
import com.andyadc.tinyrpc.transport.http.HttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

public class JdkLegacyHttpClient implements HttpClient {

	protected static final int BUFFER_SIZE = 1024;

	@Override
	public HttpResponse execute(HttpRequest request) throws IOException {
		int status;
		HttpURLConnection conn = null;
		InputStream inputStream = null;
		byte[] content = null;
		try {
			java.net.URL url = new java.net.URL(request.getUri());
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(request.getHttpMethod().name());
			conn.setConnectTimeout(request.getConnectTimeout());
			conn.setReadTimeout(request.getSocketTimeout());
			HttpHeaders headers = request.headers();
			if (headers != null) {
				for (Map.Entry<CharSequence, Object> entry : headers.getAll().entrySet()) {
					conn.setRequestProperty(entry.getKey().toString(), String.valueOf(entry.getValue()));
				}
			}
			conn.connect();

			status = conn.getResponseCode();
			if (status == HttpURLConnection.HTTP_NOT_MODIFIED) {
				return new HttpResponse(status, prarseHeader(conn));
			}
			if (status == HttpURLConnection.HTTP_OK) {
				inputStream = conn.getInputStream();
				//压缩处理
				String encoding = conn.getContentEncoding();
				if (encoding != null && !encoding.isEmpty()) {
					if (HttpHeaders.Values.GZIP.equals(encoding)) {
						inputStream = new GZIPInputStream(inputStream);
					} else if (HttpRequest.DEFLATE.equals(encoding)) {
						inputStream = new InflaterInputStream(inputStream);
					}
				}
			} else {
				inputStream = conn.getErrorStream();
			}
			//防止没有错误流
			if (inputStream != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFFER_SIZE);
				content = new byte[BUFFER_SIZE];
				int size;
				while ((size = inputStream.read(content)) != -1) {
					if (size > 0) {
						baos.write(content, 0, size);
					}
				}
				content = baos.toByteArray();
			}
		} finally {
			close(inputStream);
			close(conn);
		}
		return new HttpResponse(status, prarseHeader(conn), content);
	}

	/**
	 * 解析响应头
	 */
	protected HttpHeaders prarseHeader(final HttpURLConnection conn) {
		HttpHeaders httpHeaders = new DefaultHttpHeaders();
		Map<String, List<String>> headerFields = conn.getHeaderFields();
		if (headerFields != null && !headerFields.isEmpty()) {
			headerFields.forEach((k, v) -> {
				if (k != null && !k.isEmpty() && v != null && !v.isEmpty()) {
					httpHeaders.set(k, v.get(0));
				}
			});
		}
		return httpHeaders;
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
