package com.andyadc.test.http.claude;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class HttpClientTemplateTests {

	@Test
	public void test_get() throws Exception {
		HttpClientTemplate template = new HttpClientTemplate();
		template.get("https://www.ithome.com/", null);

		TimeUnit.SECONDS.sleep(65);
	}

}
