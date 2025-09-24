package com.andyadc.bms.security.service;

import com.andyadc.bms.redis.RedisOperator;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

	private static final Logger logger = LoggerFactory.getLogger(LoginAttemptService.class);

	private final static int MAX_ATTEMPT = 10;
	private RedisOperator redisOperator;
	private final LoadingCache<String, Integer> attemptsCache;

	public LoginAttemptService() {
		attemptsCache = CacheBuilder.newBuilder()
			.expireAfterWrite(1, TimeUnit.HOURS)
			.build(new CacheLoader<String, Integer>() {
				@Override
				public Integer load(final String key) {
					return 0;
				}
			});
	}

	public void loginSucceeded(final String key) {
		attemptsCache.invalidate(key);
		logger.info("Invalidate cache key {}", key);
	}

	public void loginFailed(final String key) {
		int attempts;
		try {
			attempts = attemptsCache.get(key);
		} catch (final ExecutionException e) {
			attempts = 0;
		}
		attempts++;
		attemptsCache.put(key, attempts);
		logger.info("put cache key {}, value {}", key, attempts);
	}

	public boolean isBlocked(final String key) {
		try {
			return attemptsCache.get(key) >= MAX_ATTEMPT;
		} catch (final ExecutionException e) {
			return false;
		}
	}

	@Inject
	public void setRedisOperator(RedisOperator redisOperator) {
		this.redisOperator = redisOperator;
	}
}
