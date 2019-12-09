package com.andyadc.codeblocks.framework.http.interceptor;

import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

/**
 * andy.an
 * 2019/12/9
 */
public class DefaultOkHttpInterceptor implements Interceptor {

	private static final Logger logger = LoggerFactory.getLogger(DefaultOkHttpInterceptor.class);

	// TODO
	@NotNull
	@Override
	public Response intercept(@NotNull Chain chain) throws IOException {
		Instant begin = Instant.now();
		Response response = chain.proceed(chain.request());
		logger.info("Request timing={}", Duration.between(begin, Instant.now()).toMillis());
		return response;
	}
}
