package com.andyadc.codeblocks.framework.http.interceptor.okhttp;

import com.andyadc.codeblocks.common.annotation.NotNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DefaultOkHttpInterceptor implements Interceptor {

	private static final Logger logger = LoggerFactory.getLogger(DefaultOkHttpInterceptor.class);

	// TODO
	@NotNull
	@Override
	public Response intercept(@NotNull Chain chain) throws IOException {
		Request request = chain.request();
		logger.info(String.format("Sending request %s on %s%n%s",
			request.url(), chain.connection(), request.headers()));

		long t1 = System.nanoTime();
		Response response = chain.proceed(request);
		long t2 = System.nanoTime();

		logger.info(String.format("Received response for %s in %.1fms %d",
			response.request().url(),
			(t2 - t1) / 1e6d,
			response.code()
			)
		);
		return response;
	}
}
