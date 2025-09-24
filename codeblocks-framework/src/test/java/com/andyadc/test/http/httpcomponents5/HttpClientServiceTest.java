package com.andyadc.test.http.httpcomponents5;

import com.andyadc.codeblocks.framework.http.httpcomponents5.HttpClientService;
import com.andyadc.codeblocks.framework.http.httpcomponents5.HttpServiceException;
import org.apache.hc.client5.http.ConnectTimeoutException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class HttpClientServiceTest {

	public static void main(String[] args) {
		HttpClientService clientService = HttpClientService.getInstance();

		// 场景1: 成功的请求
		try {
			System.out.println("--- Testing successful request ---");
			String successResponse = clientService.get("https://httpbin.org/get");
			System.out.println("Success Response: " + successResponse.substring(0, 100) + "...");
		} catch (IOException e) {
			handleException(e);
		}

		// 场景2: 请求一个不存在的页面 (404 Not Found)
		try {
			System.out.println("\n--- Testing HTTP 404 Error ---");
			clientService.get("https://httpbin.org/status/404");
		} catch (IOException e) {
			handleException(e);
		}

		// 场景3: 请求一个会超时的地址 (需要配置短超时)
		try {
			System.out.println("\n--- Testing Connection Timeout ---");
			// 10.255.255.1 是一个不可路由的IP地址, 会导致连接超时
			clientService.get("http://10.255.255.1");
		} catch (IOException e) {
			handleException(e);
		}

		// 场景4: DNS 解析失败
		try {
			System.out.println("\n--- Testing Unknown Host ---");
			clientService.get("http://invalid-and-unresolvable-hostname");
		} catch (IOException e) {
			handleException(e);
		}

		clientService.shutdown();
	}

	private static void handleException(Exception e) {
		if (e instanceof HttpServiceException hse) {
			System.err.println("HTTP Error Occurred:");
			System.err.println("  Status Code: " + hse.getStatusCode());
			System.err.println("  Response Body: " + hse.getResponseBody());
			// 决策: 4xx 错误通常不应重试。5xx 错误可以考虑重试。
		} else if (e.getCause() instanceof SocketTimeoutException || e.getCause() instanceof ConnectTimeoutException) {
			System.err.println("Network Timeout Occurred:");
			System.err.println("  Message: " + e.getMessage());
			// 决策: 网络超时是典型的可重试场景。
		} else if (e.getCause() instanceof UnknownHostException) {
			System.err.println("DNS Resolution Failed:");
			System.err.println("  Message: " + e.getMessage());
			// 决策: 这是配置错误，不应重试。需要告警通知运维人员。
		} else {
			System.err.println("An unexpected I/O error occurred:");
			e.printStackTrace();
			// 决策: 未知IO错误，谨慎重试。
		}
	}

	@Test
	public void test_xib() throws IOException {
		HttpClientService clientService = HttpClientService.getInstance();

		String url = "https://gds8443.sandbox.efaka.net/xib-merch-web/download/getReport";
		Map<String, String> params = new HashMap<>();
		params.put("", "");
		String result = clientService.postForm(url, params);
		System.out.println(result);
	}

	@Test
	public void test_get() throws Exception {
		HttpClientService clientService = HttpClientService.getInstance();
		String resp = clientService.get("https://httpbin.org/get");
		System.out.println(resp);
	}

	@Test
	public void test_get_404() throws Exception {
		HttpClientService clientService = HttpClientService.getInstance();
		String resp = clientService.get("https://httpbin.org/status/404");
		System.out.println(resp);
	}
}
