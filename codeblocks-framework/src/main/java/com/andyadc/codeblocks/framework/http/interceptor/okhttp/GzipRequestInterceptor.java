package com.andyadc.codeblocks.framework.http.interceptor.okhttp;

import com.andyadc.codeblocks.common.annotation.NotNull;
import okhttp3.*;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;

import java.io.IOException;

public class GzipRequestInterceptor implements Interceptor {

	@Override
	public Response intercept(@NotNull Chain chain) throws IOException {
		Request originalRequest = chain.request();
		if (originalRequest.body() == null || originalRequest.header("Content-Encoding") != null) {
			return chain.proceed(originalRequest);
		}

		Request compressedRequest = originalRequest.newBuilder()
			.header("Content-Encoding", "gzip")
			.method(originalRequest.method(), gzip(originalRequest.body()))
			.build();
		return chain.proceed(compressedRequest);
	}

	private RequestBody gzip(final RequestBody body) {
		return new RequestBody() {
			@Override
			public MediaType contentType() {
				return body.contentType();
			}

			@Override
			public long contentLength() {
				return -1; // We don't know the compressed length in advance!
			}

			@Override
			public void writeTo(@NotNull BufferedSink sink) throws IOException {
				BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
				body.writeTo(gzipSink);
				gzipSink.close();
			}
		};
	}
}
