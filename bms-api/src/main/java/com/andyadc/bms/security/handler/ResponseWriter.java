package com.andyadc.bms.security.handler;

import com.andyadc.bms.security.SecurityConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.PrintWriter;

public abstract class ResponseWriter {

	private static final Logger logger = LoggerFactory.getLogger(ResponseWriter.class);

	private final ObjectMapper objectMapper;

	public ResponseWriter(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public void write(HttpServletRequest request, HttpServletResponse response, Throwable throwable) throws IOException {
		if (response.isCommitted()) {
			logger.warn("Response has already been committed.");
			return;
		}

		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(SecurityConstants.DEFAULT_CHARACTER_ENCODING);

		String resBody = objectMapper.writeValueAsString(this.body(request, throwable));

		PrintWriter printWriter = response.getWriter();
		printWriter.print(resBody);
		printWriter.flush();
		printWriter.close();
	}

	/**
	 * @return response info
	 */
	protected abstract Object body(HttpServletRequest request, Throwable throwable);
}
