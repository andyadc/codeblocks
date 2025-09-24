package com.andyadc.test.http;

import com.andyadc.codeblocks.framework.http.HttpClientTemplate;
import com.andyadc.codeblocks.framework.http.HttpComponentsClientTemplate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class XibTests {

	static HttpClientTemplate template = null;

	private static void initHttpComponentsClient() {
		template = new HttpComponentsClientTemplate();
//		template = new OkHttpClientTemplate();
		template.init();
	}

	@BeforeAll
	public static void init() {
		initHttpComponentsClient();
	}

	@AfterAll
	public static void close() throws Exception {
		if (template != null) {
			template.close();
		}
	}

	@Test
	public void testDownload() {
		String url = "https://gds8443.sandbox.efaka.net/xib-merch-web/download/getReport";
		url = "http://localhost:8080/test/4";
		url = "http://localhost:9999/servlet/echo";

		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		headers.put("Authorization", "Bearer NDNkMjQ1NDMtNjNmOS00Nw==");

		Map<String, String> parameters = new HashMap<>();
		parameters.put("entityId", "KQ53281306");
		parameters.put("entityType", "PF");
		parameters.put("merchantId", "KQ53281306");
		parameters.put("reportType", "transaction");
		parameters.put("date", "20250529");

		template.form(url, parameters, headers);

	}

}
