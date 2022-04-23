package com.andyadc.bms.service;

import com.andyadc.bms.redis.RedisOperator;
import com.andyadc.bms.security.model.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AuthTokenService {

	private static final Logger logger = LoggerFactory.getLogger(AuthTokenService.class);
	private final String keyPrefix = String.join(":", "auth", "token");
	private RedisOperator redisOperator;

	@Inject
	public void setRedisOperator(RedisOperator redisOperator) {
		this.redisOperator = redisOperator;
	}

	public void getAuthToken() {

	}

	public boolean saveAuthToken(UserContext userContext) {
		Map<String, Object> kvs = new LinkedHashMap<>(2);
		kvs.put(String.join(":", "auth", "user", userContext.getUsername()), userContext.getToken());
		kvs.put(String.join(":", "auth", "token", userContext.getToken()), userContext);

		try {
			redisOperator.batchSet(kvs, 1L, TimeUnit.HOURS);
			return true;
		} catch (Exception e) {
			logger.error("SaveAuthToken {} error", userContext, e);
		}
		return false;
	}

	public boolean removeAuthToken(UserContext userContext) {
		String userkey = String.join(":", "auth", "user", userContext.getUsername());
		String tokenkey = String.join(":", "auth", "token", userContext.getToken());
		try {

			return redisOperator.del(userkey) > 0L;
		} catch (Exception e) {
			logger.error("RemoveAuthToken {} error", userContext, e);
		}
		return false;
	}
}
