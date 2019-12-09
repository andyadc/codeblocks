package framework.test;

import com.andyadc.codeblocks.framework.concurrent.ThreadPoolCreator;
import com.andyadc.codeblocks.framework.http.HttpClientTemplate;
import com.andyadc.codeblocks.framework.http.OkHttpClientTemplate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * andy.an
 * 2019/12/6
 */
public class HttpClientTemplateTest {

	HttpClientTemplate template = null;

	@Before
	public void init() {
//		template = new HttpComponentsClientTemplate();
		template = new OkHttpClientTemplate();
		template.init();
	}

	@Test
	public void testConcurrentGet() throws Exception {
		int count = 100;
		CountDownLatch latch = new CountDownLatch(count);

		ThreadPoolExecutor executor = new ThreadPoolCreator().create();
		for (int i = 0; i < count; i++) {
			executor.execute(() -> {
				try {
					String response = template.get("https://www.baidu.com/");
					System.out.println(Thread.currentThread().getName() + " >>> " + response);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				} finally {
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
