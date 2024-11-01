package com.andyadc.bms.security.service;

import com.andyadc.bms.common.Constants;
import com.andyadc.bms.redis.RedisOperator;
import com.andyadc.bms.security.handler.TokenStorage;
import com.andyadc.bms.security.model.SecureUser;
import com.andyadc.bms.security.model.TokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

// TODO
@Service
public class RedisTokenStorage implements TokenStorage {

	private static final Logger logger = LoggerFactory.getLogger(RedisTokenStorage.class);

	private static final String KEY_PREFIX = Constants.REDIS_CACHE_PREFIX + "jwt:user:token:";
	private final RedisOperator redisOperator;

	public RedisTokenStorage(RedisOperator redisOperator) {
		this.redisOperator = redisOperator;
	}

	@Override
	public void expire(SecureUser user) {
		String userId = user.getUserId();
		redisOperator.del(KEY_PREFIX + userId);
	}

	@Override
	public TokenResponse get(SecureUser user) {

		return null;
	}
}
