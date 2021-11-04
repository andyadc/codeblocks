package com.andyadc.test.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * cookie: https://www.baeldung.com/httpclient-4-cookies
 */
public class HttpClientTests {

	/**
	 * A very important element is the domain being set on the cookie – without setting the proper domain,
	 * the client will not send the cookie at all!
	 */
	@Test
	public void whenSettingCookiesOnTheHttpClient_thenCookieSentCorrectly() throws IOException {
		BasicCookieStore cookieStore = new BasicCookieStore();
		BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", "1234");
		cookie.setDomain("localhost"); //
		cookie.setPath("/");
		cookie.setAttribute(ClientCookie.DOMAIN_ATTR, "true");

		cookieStore.addCookie(cookie);
		HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();

		final HttpGet request = new HttpGet("http://localhost:9999/echo/hello/");

		HttpResponse response = client.execute(request);
		Assertions.assertEquals(response.getStatusLine().getStatusCode(), 200);
	}
}
