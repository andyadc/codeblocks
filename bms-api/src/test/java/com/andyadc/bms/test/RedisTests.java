package com.andyadc.bms.test;

import com.andyadc.bms.auth.dto.AuthUserDTO;
import com.andyadc.bms.common.Response;
import com.andyadc.bms.redis.RedisOperator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTests {

	@Inject
	private ObjectMapper objectMapper;
	@Inject
	private RedisTemplate<String, Object> redisTemplate;
	@Inject
	private RedisOperator redisOperator;

	@Test
	public void testSet() {
//		redisOperator.set("a", 12345);
//		System.out.println(redisOperator.get("a"));

		AuthUserDTO dto = new AuthUserDTO();
		dto.setId(Long.MAX_VALUE);
		dto.setStatus(0);
		dto.setUsername("adc");
		Response<AuthUserDTO> response = Response.success(dto);
		String key = String.join(":", "auth", "user", dto.getUsername());
		redisOperator.set(key, response);
	}

	@Test
	public void testGet() {
		String key = String.join(":", "auth", "user", "adc");
		Object value = redisOperator.get(key);
		System.out.println(value);
		System.out.println(value instanceof String);

		Response<AuthUserDTO> response = objectMapper.convertValue(value, new TypeReference<Response<AuthUserDTO>>() {
		});
		System.out.println(response);

//		Response<AuthUserDTO> response = objectMapper.convertValue(value, Response.class);
//		objectMapper.readValue(value, new TypeReference<Response<AuthUserDTO>>(){});
//		System.out.println(response);
	}

	@Test
	public void testMQ() {
		for (int i = 0; i < 10; i++) {
			redisTemplate.opsForList().leftPush("task", "data-" + i);
		}
		Object task = redisTemplate.opsForList().rightPop("task");
		System.out.println(">>> " + task);
	}

	@Test
	public void testExpire() {
		System.out.println(redisOperator.expire("a", 60L, TimeUnit.SECONDS));
	}

	@Test
	public void testExist() {
		System.out.println(redisOperator.exist(Arrays.asList("a", "b")));
	}

	@Test
	public void testScanKeys() {
		Set<String> keys = redisOperator.keys("a*", 100L);
		System.out.println(keys);
	}

	@Test
	public void testRedisTemplate() {
		redisTemplate.opsForValue().set("u1", new AuthUserDTO());
		System.out.println(redisTemplate.opsForValue().get("u1"));
	}

	@Test
	public void testRedisOperator() {
//        redisOperator.set("u2", new User());
//        redisOperator.set("u3", new User());
//        System.out.println(redisOperator.get("u1"));

		List<String> keys = Arrays.asList("a1", "a2", "a3");
		List<Object> values = redisOperator.multiGet(keys);
		System.out.println(values);
	}

	@Test
	public void testPipeline() {
		Map<String, Object> map = new HashMap<>();
		map.put("a1", new AuthUserDTO(1L, "a1", "aaaaaa"));
		map.put("a2", new AuthUserDTO(2L, "a2", "bbbbbb"));
		map.put("a3", new AuthUserDTO(3L, "a3", "vvvvvv"));
		redisOperator.batchSet(map, null, null);
	}
}
