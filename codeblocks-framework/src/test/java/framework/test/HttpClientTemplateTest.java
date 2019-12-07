package framework.test;

import com.andyadc.codeblocks.framework.http.HttpClientTemplate;
import com.andyadc.codeblocks.framework.http.HttpComponentsClientTemplate;
import com.andyadc.codeblocks.framework.http.OkHttpClientTemplate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
	public void testGet() {
		String response = template.get("https://www.baidu.com/");
		System.out.println(response);
	}

	@After
	public void close() throws Exception {
		template.close();
	}

}
