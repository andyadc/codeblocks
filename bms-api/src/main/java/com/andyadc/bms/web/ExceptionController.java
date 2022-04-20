package com.andyadc.bms.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.concurrent.TimeUnit;

@RequestMapping("/ex")
@RestController
public class ExceptionController {

	@RequestMapping("/success")
	public ResponseEntity<Object> success() {
		return ResponseEntity.ok("success");
	}

	@RequestMapping("/timeout")
	public ResponseEntity<Object> timeout() throws Exception {
		TimeUnit.SECONDS.sleep(10L);
		return ResponseEntity.ok("timeout");
	}

	@RequestMapping("/exception")
	public Object exception() {
		return new SQLIntegrityConstraintViolationException("OHO");
	}
}
