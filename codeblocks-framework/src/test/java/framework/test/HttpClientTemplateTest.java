package framework.test;

import com.andyadc.codeblocks.framework.http.HttpClientTemplate;
import com.andyadc.codeblocks.framework.http.HttpComponentsClientTemplate;
import org.junit.Test;

/**
 * andy.an
 * 2019/12/6
 */
public class HttpClientTemplateTest {

	@Test
	public void testGet() {
		HttpClientTemplate template = new HttpComponentsClientTemplate();
		String response = template.get("https://www.baidu.com/");
		System.out.println(response);
	}
}
