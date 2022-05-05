package com.andyadc.bms.service;

import com.andyadc.bms.modules.auth.dto.AuthUserDTO;
import com.andyadc.bms.modules.auth.entity.AuthUser;
import com.andyadc.bms.modules.auth.service.AuthUserService;
import com.andyadc.bms.redis.RedisOperator;
import com.andyadc.bms.security.exception.SmsSendException;
import com.andyadc.codeblocks.kit.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

@Service
public class MobileService {

	private static final Logger logger = LoggerFactory.getLogger(MobileService.class);

	private AuthUserService authUserService;
	private RedisOperator redisOperator;

	public void sendSms(AuthUserDTO userDTO) {
		String phoneNo = userDTO.getPhoneNo();
		String countKey = String.join(":", "mobile", "login", "count", phoneNo);
		Object count = redisOperator.get(countKey);
		int sendCount = 0;
		if (count != null) {
			sendCount = (int) count;
			if (sendCount > 6) {
				logger.warn("Sms send exceed. phoneNo: {}", phoneNo);
				throw new SmsSendException("短信发送异常");
			}
		}

		AuthUser authUser = authUserService.findUserByPhoneNo(phoneNo);
		if (authUser == null) {
			logger.warn("User does not exists. phoneNo: {}", phoneNo);
			throw new SmsSendException("短信发送异常");
		}

		String randomNum = RandomUtil.genRandomNum(6);
		if (logger.isDebugEnabled()) {
			logger.debug("Mobile login sms code: {}", randomNum);
		}
		String smsKey = String.join(":", "mobile", "login", phoneNo);
		redisOperator.set(smsKey, randomNum, 10L, TimeUnit.MINUTES);

		sendCount++;
		redisOperator.set(countKey, sendCount, 1L, TimeUnit.DAYS);
	}

	public boolean checkVerificationCode(String phoneNo, String verificationCode) {
		String smsKey = String.join(":", "mobile", "login", phoneNo);
		Object value = redisOperator.get(smsKey);
		if (value == null) {
			return false;
		}

		String code = (String) value;
		return verificationCode.equals(code);
	}

	@Inject
	public void setAuthUserService(AuthUserService authUserService) {
		this.authUserService = authUserService;
	}

	@Inject
	public void setRedisOperator(RedisOperator redisOperator) {
		this.redisOperator = redisOperator;
	}
}
