package com.andyadc.bms.web;

import com.andyadc.bms.service.ExceptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RequestMapping("/exception")
@RestController
public class ExceptionController {

	private final ExceptionService exceptionService;

	public ExceptionController(ExceptionService exceptionService) {
		this.exceptionService = exceptionService;
	}

	@RequestMapping("/success")
	public ResponseEntity<Object> success() {
		return ResponseEntity.ok("success");
	}

	@RequestMapping("/timeout")
	public ResponseEntity<Object> timeout() throws Exception {
		TimeUnit.SECONDS.sleep(10L);
		return ResponseEntity.ok("timeout");
	}

	@RequestMapping("/throw")
	public Object exception() {
		Object ret = exceptionService.throwException();
		return ResponseEntity.ok(ret);
	}
}
