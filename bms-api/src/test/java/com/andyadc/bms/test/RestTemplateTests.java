package com.andyadc.bms.test;

import com.andyadc.codeblocks.kit.idgen.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class RestTemplateTests {

	@Inject
	private RestTemplate restTemplate;

	@Test
	public void testGet() {
		ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:8080/", String.class);
		System.out.println(entity.getBody());
	}

	@Test
	public void testExchange() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("requestId", UUID.randomUUID());

		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("requestId", UUID.randomUUID());

		HttpEntity<Object> entity = new HttpEntity<>(reqMap, headers);

		ResponseEntity<Map> responseEntity = restTemplate.exchange(
			"http://dcep-inner.99bill.com/dcep-bank-cib/activity/redpacket/sync",
			HttpMethod.POST,
			entity,
			Map.class);
		System.out.println(responseEntity.getBody());
	}

	@Test
	public void testExeute() {

		restTemplate.execute(
			"http://dcep-inner.99bill.com/dcep-bank-cib/activity/redpacket/sync",
			HttpMethod.POST,
			new RequestCallback() {
				@Override
				public void doWithRequest(ClientHttpRequest request) throws IOException {
					// manipulate request headers and body
				}
			},
			new ResponseExtractor<Object>() {
				@Override
				public Object extractData(ClientHttpResponse response) throws IOException {
					// manipulate response and return ResponseEntity
					return null;
				}
			}
		);
	}

}
