package com.andyadc.bms.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.inject.Inject;

@EnableCaching
@Configuration
public class RedisConfig {

	private ObjectMapper objectMapper;
	private final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

	@Inject
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Bean(name = "redisTemplate")
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.setHashKeySerializer(stringRedisSerializer);

		RedisSerializer<Object> jacksonRedisSerializer = buildJacksonRedisSerializer();
		redisTemplate.setValueSerializer(jacksonRedisSerializer);
		redisTemplate.setHashValueSerializer(jacksonRedisSerializer);

		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	private RedisSerializer<Object> buildJacksonRedisSerializer() {
		Jackson2JsonRedisSerializer<Object> redisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		redisSerializer.setObjectMapper(objectMapper);
		return redisSerializer;
	}
}
