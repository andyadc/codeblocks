package com.andyadc.bms.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

@RequestMapping("/callable")
@RestController
public class CallableController {

	private static final Logger logger = LoggerFactory.getLogger(CallableController.class);

	@RequestMapping(value = "/")
	public Callable<String> callable() {
		logger.info("controller called. Thread: " + Thread.currentThread().getName());

		Callable<String> callable = () -> {
			logger.info("async task started. Thread: " + Thread.currentThread().getName());
			Thread.sleep(300L);
			logger.info("async task finished");
			return "async result";
		};

		logger.info("controller finished");
		return callable;
	}
}
