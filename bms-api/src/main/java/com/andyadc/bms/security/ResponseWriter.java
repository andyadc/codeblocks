package com.andyadc.bms.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public abstract class ResponseWriter {

	private static final Logger logger = LoggerFactory.getLogger(ResponseWriter.class);

	private final ObjectMapper objectMapper;

	public ResponseWriter(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public void write(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (response.isCommitted()) {
			logger.info("Response has already been committed");
			return;
		}

		response.setStatus(HttpServletResponse.SC_OK);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		String resBody = objectMapper.writeValueAsString(this.body(request));
		PrintWriter printWriter = response.getWriter();
		printWriter.print(resBody);
		printWriter.flush();
		printWriter.close();
	}

	protected abstract Map<String, Object> body(HttpServletRequest request);
}
