package com.andyadc.bms.security.handler;

import com.andyadc.bms.common.RespCode;
import com.andyadc.bms.common.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 处理认证失败的逻辑
 */
@Component
public class RestAuthenticationEntryPoint extends ResponseWriter implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(RestAuthenticationEntryPoint.class);

	public RestAuthenticationEntryPoint(ObjectMapper objectMapper) {
		super(objectMapper);
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
		logger.error("Responding with unauthorized error. {}", e.getMessage());
//		response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
		this.write(request, response, e);
	}

	@Override
	protected Object body(HttpServletRequest request, Throwable throwable) {
		Response<Object> resp = Response.of(RespCode.UNAUTHORIZED);
		Map<String, Object> data = new LinkedHashMap<>(2);
		data.put("uri", request.getRequestURI());
		data.put("exception", throwable.getMessage());
		resp.setData(data);
		return resp;
	}
}
