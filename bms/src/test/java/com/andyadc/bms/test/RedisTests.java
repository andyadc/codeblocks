package com.andyadc.bms.test;

import com.andyadc.bms.auth.dto.AuthUserDTO;
import com.andyadc.bms.cache.redis.RedisOperator;
import com.andyadc.bms.common.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTests {

	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	@Resource
	private RedisOperator redisOperator;

	@Test
	public void testGet() {
		Object o = redisOperator.get(Constants.REDIS_CACHE_PREFIX + "auth:user:" + "adc");
		System.out.println(o);
		AuthUserDTO dto = objectMapper.convertValue(o, AuthUserDTO.class);
		System.out.println(dto);
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
		System.out.println(redisOperator.expire("a", 60, TimeUnit.SECONDS));
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
		redisOperator.batchSet(map);
	}
}
