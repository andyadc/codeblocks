package com.andyadc.test.http;

import com.andyadc.codeblocks.common.JsonUtils;
import com.andyadc.codeblocks.framework.http.HttpClientTemplate;
import com.andyadc.codeblocks.framework.http.HttpComponentsClientTemplate;
import com.andyadc.codeblocks.framework.http.interceptor.httpcomponents.DefaultRequestInterceptor;
import com.andyadc.codeblocks.framework.http.interceptor.httpcomponents.DefaultResponseInterceptor;
import com.andyadc.codeblocks.kit.concurrent.ThreadUtil;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MAMTests {

	private static final Logger logger = LoggerFactory.getLogger(MAMTests.class);

	private static final List<HttpRequestInterceptor> requestInterceptorList;
	private static final List<HttpResponseInterceptor> responseInterceptorList;
	static HttpClientTemplate template = null;

	static {
		requestInterceptorList = List.of(
			new DefaultRequestInterceptor()
		);
		responseInterceptorList = List.of(
			new DefaultResponseInterceptor()
		);
	}

	private static void initHttpComponentsClient() {
		template = new HttpComponentsClientTemplate();
		((HttpComponentsClientTemplate) template).setRequestInterceptors(requestInterceptorList);
		((HttpComponentsClientTemplate) template).setResponseInterceptors(responseInterceptorList);
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
	public void testGlbNotify() {
		Map<String, Object> params = new HashMap<>();
		params.put("skey", "DL4vjIfxkNJB3irUvNgtqWSZiH8x/yVhRurhk4OzmBzEpHA2EzgqBrovz48KaXlxq0VmN9FmVTNfYpA2z7kVKni9xdH7Y3ar4Y22FPxk5A+Ed6xL3E04ioUvTtHJaoi/IHH0mRAK56MjFaEDN6xF6BjtZWnbgpHkSLn7LuA5LEc/499spuza3JAbw6KBz4VVJJKKfJVHo1R7SFnE8OjbCOiJte2rKhqVt/dHTc2q2M3+ytAwJk2tR7quWnRUBVfrOtOr954pbQPyQx7qIp+NFCZg/v0hnGIOxm4tY3puhW5XASVQ1kjd5YmGv4+5DD9+7ovfFM9RDpB62rdEavWdlg==");
		params.put("body", "04n5SzI6kYB0ACeBj6reDN+GKkq5b+ehZaPc8pmsLdVwtfakBYVCBiKWgl0xKrB6n/qh0yWzxiibgMgfFB5XlyfbIWkdDzOz4713nVVjGssucoQYFxG/61Z57PGwkZr2mm5WgfPQn53PADM+H9QIFjhZ8sJHg4DiMkrKwBEDkprY1dCqu0/tWzYIjbjzNBDU0yJkm51TlfcJ7zfq3aomP2rt1uTx6vM9xlbST2dNdwgCQAYyeWz3DQjuxxmftA3PZwAFgER+a4VprIRDQ/yNNWwH1t8MMFw7SuY+RMAim0flHiEje0v4oI8BtImW/AKFyaoE4euR7dupWsJIct/reDKfH17o+x8E4MMAsq7gUuaSofnVCA/kfVFWNGKknQDFEujbZkHgJ76JffeVFNUXs4S0EK0KamMVpOylW1kCW1l3UQ4m9q+u6u7V7cgiNNsnRU/CIvykvjOwLNrxmpBYCfPbZlx49/0wiMDo6qLygjDDCXilHleM67Hb7JJMEUxGTP6Ja9sEWKlxS56HhH/i1MXM/SMqt9xB/IF93tJ0+FtdghaIEhClxSgERTdL34/JnIzlg3+kjOpQ65jx/0HcRKkmxQ16pX/3cw5KKYQCIStkoiOCLKJ9MF1LDmUm11hSSSaCrEZiURD2Eef4j1TkK3hbOsJxIfnyw/M34FItIHFbS0AUNNb2ZxklxpF6JF4F9QorlB3MU2C+FVdPE0mjMyyIteG7xdZiHBHREXjWBiHFXwsfmiAKc9MbvSsJ22GhuzDbWOqIr5vbxBhjJknAAKMFvTe7Wn/vDgykiYgyjzBVGkhVcEO+fRcSUmVsGRtP8zN0k5mZae4WS49JGwGfvS67Yh2WI9RrL71++bS2L9mVhBEOZuluvYzkFpWDgY/qrxgThqgye300srpzie6zmBMgISti/RlFMwMqdVuY6b0Vrt0mVvNp9PP2sM9aqwkdGsB30lTeu51Bmr29t4+tBlz2ghe7I7zcv0HWrpworYBCypb4URNQM+giP28QxbpDfrysbt5JOb30yenguxJsbcz7RJVfJzZtjkNL661xv2jxqQsU3lp5uaZ/KcwKQeX3nf+XsS9FLlfd+1VRxxPXdrPnveArcvDUu2jXGkkKkm8Rh/0WVD58DzKfeAU9QxNdHrGL9Ki5P6hGdSwiv22fCLgaXWHpCThkhVoaRhEliNHSB5Nc8E39GYN17p5jAG3SEMl8c58vink1ljLAw2OwsL400LsZIr6NFWhnujNx4qk1PHtSE2ZDH7X3HVj7JKvro97dChLnf9k5BUeISQ+eF7rcTveyTQeRvoUP/CjZdsYiPrzKBgY+JmRPlz07ieJw4cSYiAzfr4PrRMvvOStw5EPUIv/XwI7Fi7TgPR28EHy7Zn4Up1WqImgAh5/eD+H63HPpuOla6aBxy0IqDCeW2xdqI7h6IaOf7nrNWnoGN1d4+tGJJ2XJmtCDHOkz6HzMx0/friI20V4xc5FJ1fWMRdhnx5HIia3cs7nmSWqWfGrIq5Spj9UEgx64J0/t/TgwYpi8bcrznlyCb6TGVrpqQw==");

		Map<String, String> headers = new HashMap<>();
		headers.put("signature", "E7ZuoUISjU7wUh5sRJxwnuaUD33WecELwRLTFxTXHqDAJPUUsAfqEGPJlxOJvZUGGWw2wnX0t4sgT+M9OEVXTzPWJ8VovbZwUOLSRUSCt8NmAUJ7HV8+gOVyXreWIXOa4pcdsg3E8ePj1attsbh8Bv+nXP5u+5lMIdBfr28L9800hb4FBLle0yaqgNAMmYEi9eHLma8igSu+fwKS9WSO+oVkEK7zeqEfM9/ki6gDYMrXjZt86Z0OU6C3HEQSU71E/TuZ2yXVkJtsATW+m+ThSleBdlZo5sZq5JlxgD7KTm5Sw4+Eos2WJDQfnKFIBRJxEiJy5rQ6QDbfBIQnmPSBPw==");
		headers.put("signtype", "RSA2");
		headers.put("interaction_id", "vcpcbes-exp.ack0001");
		headers.put("random_id", UUID.randomUUID().toString().replace("-", ""));

		String url = "http://global.99bill.net/global-frontend/notify/boc/export";
		String result = template.post(url, JsonUtils.toJSONString(params), null, headers);
		System.out.println(result);
	}

	@Test
	public void testPartnerConfigQuery() {
		Map<String, Object> params = new HashMap<>();
		params.put("traceId", "a_local_" + System.currentTimeMillis());
		params.put("appId", "324");
		params.put("accessToken", "97LS8LK4CEJIGKU8");
		params.put("bizCode", "mam.mam-prod-operation");
		params.put("requestTime", new Date());

		params.put("platformCode", "10222271557");
		params.put("memberCode", "10222271557");
		params.put("keys", List.of("withdraw.time.mode"));

		String url = "http://mam.99bill.net/mam-prod-operation/dispatcher/index"
			+ "?targetUrl="
			+ "http://mam-inner.99bill.com/hat-partner/config/query-effective";

		for (int i = 0; i < 10; i++) {
			String result = template.post(url, JsonUtils.toJSONString(params), null);
			System.out.println(result);

			ThreadUtil.sleep(1000L);
		}

	}
}
