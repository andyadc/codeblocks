package com.andyadc.test.http.httpcomponents4;

import com.andyadc.codeblocks.framework.http.httpcomponents4.SimplyHttpClient;
import org.junit.jupiter.api.Test;

public class SimplyHttpClientTests {

	@Test
	public void test() throws Exception {
		SimplyHttpClient httpClient = SimplyHttpClient.getInstance();
		String url = "https://utpuat.onebanktest.com:28443/v2/account/new_open_account";
		url = "https://www.ithome.com/";
		String result = httpClient.doGet(url);
		System.out.println(result);
	}

}
