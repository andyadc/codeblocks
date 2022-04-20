package com.andyadc.bms.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;

@SpringBootTest
public class RestTemplateTests {

	@Inject
	private RestTemplate restTemplate;

	@Test
	public void test() {
		ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:8080/", String.class);
		System.out.println(entity.getBody());
	}
}
