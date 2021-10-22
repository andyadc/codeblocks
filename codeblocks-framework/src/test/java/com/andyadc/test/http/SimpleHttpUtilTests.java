package com.andyadc.test.http;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class SimpleHttpUtilTests {

	@Test
	public void test001() throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("name", "adc");
		params.put("password", "123456");
		String url = "http://localhost:9999/hello/";
		SimpleHttpUtil.doPost(url, params, 5000, 5000);
	}
}
