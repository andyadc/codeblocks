package com.andyadc.boot.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class RestTemplateTests {

	@Autowired
	private RestTemplate restTemplate;

	@Test
	public void testGet() {
		ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:8080/exception/success", String.class);
		System.out.println(entity.getBody());
	}

	@Test
	public void testTimeout() {
		ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:8080/exception/timeout", String.class);
		System.out.println(entity.getBody());
	}

	@Test
	public void testThrow() {
		ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:8080/exception/throw", String.class);
		System.out.println(entity.getBody());
	}
}
