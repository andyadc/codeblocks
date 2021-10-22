package com.andyadc.test.http;

import com.andyadc.codeblocks.framework.concurrent.ThreadPoolCreator;
import com.andyadc.codeblocks.framework.http.HttpClientTemplate;
import com.andyadc.codeblocks.framework.http.HttpComponentsClientTemplate;
import com.andyadc.codeblocks.framework.http.OkHttpClientTemplate;
import com.andyadc.codeblocks.framework.http.interceptor.httpcomponents.DefaultRequestInterceptor;
import com.andyadc.codeblocks.framework.http.interceptor.httpcomponents.DefaultResponseInterceptor;
import com.andyadc.codeblocks.framework.http.interceptor.okhttp.DefaultOkHttpInterceptor;
import com.andyadc.codeblocks.kit.idgen.UUID;
import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * andy.an
 * 2019/12/6
 */
public class HttpClientTemplateTest {

	private static final Logger logger = LoggerFactory.getLogger(HttpClientTemplateTest.class);
	private static final List<HttpRequestInterceptor> requestInterceptorList;
	private static final List<HttpResponseInterceptor> responseInterceptorList;
	private static final List<Interceptor> interceptorList;
	static HttpClientTemplate template = null;

	static {
		requestInterceptorList = Arrays.asList(
			new DefaultRequestInterceptor()
		);
		responseInterceptorList = Arrays.asList(
			new DefaultResponseInterceptor()
		);

		HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
		loggingInterceptor.level(HttpLoggingInterceptor.Level.HEADERS);
		interceptorList = Arrays.asList(
			new DefaultOkHttpInterceptor(),
			loggingInterceptor
		);
	}

	@BeforeAll
	public static void init() {
//		initOkHttpClient();
//		initHttpComponentsClient();
	}

	private static void initHttpComponentsClient() {
		template = new HttpComponentsClientTemplate();
		((HttpComponentsClientTemplate) template).setRequestInterceptors(requestInterceptorList);
		((HttpComponentsClientTemplate) template).setResponseInterceptors(responseInterceptorList);
		template.init();
	}

	private static void initOkHttpClient() {
		template = new OkHttpClientTemplate();
		((OkHttpClientTemplate) template).setInterceptors(interceptorList);
		template.init();
	}

	@AfterAll
	public static void close() throws Exception {
		if (template != null) {
			template.close();
		}
	}

	@Test
	public void testHttpComponentsClient() {
		initHttpComponentsClient();

		String json = "{\"name\": \"adc\"}";
		Map<String, String> params = new HashMap<>();
		params.put("name", "aaaa");

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("header1", "header1");

//		String result = template.post("http://localhost:9999/hello/", json, params);
		String result = template.form("http://localhost:9999/hello/", params, headerMap);

		System.out.println(result);
	}

	@Test
	public void testOkHttpClient() {
		initOkHttpClient();

		String json = "{\"name\": \"adc\"}";
		Map<String, String> params = new HashMap<>();
		params.put("name", "aaaa");

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("header1", "header1");

//		String result = template.post("http://localhost:9999/hello/", json, params);
		String result = template.form("http://localhost:9999/hello/", params, headerMap);

		System.out.println(result);
	}

	@Test
	public void testConcurrentGet() throws Exception {
		int count = 100;
		CountDownLatch latch = new CountDownLatch(count);

		ThreadPoolExecutor executor = ThreadPoolCreator.create();
		for (int i = 0; i < count; i++) {
			executor.execute(() -> {
				try {
					MDC.put("traceId", UUID.randomUUID());
					String response = template.get("https://www.baidu.com/");
					logger.info(Thread.currentThread().getName() + " >>> " + response);
				} catch (Exception e) {
					logger.error(e.getMessage());
				} finally {
					MDC.clear();
					latch.countDown();
				}
			});
		}

		latch.await();
		executor.shutdown();
	}

	@Test
	public void testGet() {
		String response = template.get("https://www.baidu.com/");
		System.out.println(response);
	}
}
