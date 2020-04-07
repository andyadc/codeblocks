package framework.test;

import com.andyadc.codeblocks.framework.concurrent.ThreadPoolCreator;
import com.andyadc.codeblocks.framework.http.HttpClientTemplate;
import com.andyadc.codeblocks.framework.http.HttpComponentsClientTemplate;
import com.andyadc.codeblocks.framework.http.OkHttpClientTemplate;
import com.andyadc.codeblocks.framework.http.interceptor.DefaultOkHttpInterceptor;
import com.andyadc.codeblocks.framework.http.interceptor.DefaultRequestInterceptor;
import com.andyadc.codeblocks.framework.http.interceptor.DefaultResponseInterceptor;
import com.andyadc.codeblocks.kit.idgen.UUID;
import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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

	HttpClientTemplate template = null;

	private static List<HttpRequestInterceptor> requestInterceptorList;
	private static List<HttpResponseInterceptor> responseInterceptorList;

	private static List<Interceptor> interceptorList;

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

	@Before
	public void init() {
		initOkHttpClient();
//		initHttpComponentsClient();
	}

	private void initHttpComponentsClient() {
		template = new HttpComponentsClientTemplate();
		((HttpComponentsClientTemplate) template).setRequestInterceptors(requestInterceptorList);
		((HttpComponentsClientTemplate) template).setResponseInterceptors(responseInterceptorList);
		template.init();
	}

	private void initOkHttpClient() {
		template = new OkHttpClientTemplate();
		((OkHttpClientTemplate) template).setInterceptors(interceptorList);
		template.init();
	}

	@Test
	public void testRequest() {
		String json = "{\"name\": \"adc\"}";
		Map<String, String> params = new HashMap<>();
		params.put("name", "aaaa");
		String result = template.post("http://localhost:9999/echo", json, params);

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

	@After
	public void close() throws Exception {
		template.close();
	}

}
