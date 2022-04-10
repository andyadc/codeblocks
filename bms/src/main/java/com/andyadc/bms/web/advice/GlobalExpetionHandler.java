package com.andyadc.bms.web.advice;

import com.andyadc.bms.common.Response;
import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalExpetionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExpetionHandler.class);

	/**
	 *
	 */
	@ExceptionHandler(value = {MethodArgumentNotValidException.class})
	public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		String stackTraceAsString = Throwables.getStackTraceAsString(e);
		logger.error(stackTraceAsString);

		String defaultMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

		return ResponseEntity.ok(Response.of("400", defaultMessage));
	}

	/**
	 *
	 */
	@ExceptionHandler(value = {SQLIntegrityConstraintViolationException.class})
	public ResponseEntity<Object> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
		String stackTraceAsString = Throwables.getStackTraceAsString(e);
		logger.error(stackTraceAsString);

		return ResponseEntity.ok(Response.of("400", "数据重复"));
	}
}
