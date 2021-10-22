package com.andyadc.test.http;

import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class SpringHttpTests {

	@Test
	public void testUriComponents() {
		UriComponents uriComponents = UriComponentsBuilder
			.fromHttpUrl("http://localhost:9999/hello/")
			.query("abc")
			.build();
		System.out.println(uriComponents.toUriString());
	}
}
