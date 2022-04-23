package com.andyadc.bms.web.advice;

import com.andyadc.bms.common.ErrorResponse;
import com.andyadc.bms.common.Response;
import com.andyadc.bms.exception.IllegalRequestException;
import com.andyadc.bms.security.exception.JwtExpiredTokenException;
import com.google.common.base.Throwables;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalExpetionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExpetionHandler.class);

	// @ExceptionHandler(value = {JwtExpiredTokenException.class})
	public ResponseEntity<Object> handlerJwtExpiredTokenException(JwtExpiredTokenException e, HttpServletRequest request) {
		ErrorResponse response = new ErrorResponse();
		response.setStatus(HttpStatus.UNAUTHORIZED);
		response.setMessage(e.getMessage());
		response.setPath(request.getServletPath());

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = IllegalRequestException.class)
	public ResponseEntity<Object> handleIllegalRequestException(IllegalRequestException e, HttpServletRequest request) {
		String stackTrace = ExceptionUtils.getStackTrace(e);
		logger.error(stackTrace);

		String path = request.getServletPath();

		ErrorResponse response = new ErrorResponse();
		response.setStatus(HttpStatus.BAD_REQUEST);
		response.setMessage(e.getMessage());
		response.setPath(path);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

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
