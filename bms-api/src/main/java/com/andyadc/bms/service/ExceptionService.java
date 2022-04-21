package com.andyadc.bms.service;

import com.andyadc.bms.common.RespCode;
import com.andyadc.bms.common.Response;
import com.andyadc.bms.exception.IllegalRequestException;
import com.andyadc.codeblocks.kit.concurrent.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ExceptionService {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionService.class);
	private int count = 1;

	public Object throwException() {
		count++;
		logger.info("throwException {}", count);

		if (count % 2 == 0) {
			ThreadUtil.sleep(count * 10L, TimeUnit.MILLISECONDS);
			throw new IllegalRequestException("Ops!");
		}

		return Response.of(RespCode.SUCC);
	}
}
