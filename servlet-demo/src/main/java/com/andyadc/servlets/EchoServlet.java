package com.andyadc.servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;

@WebServlet(name = "echoServlet", value = "/echo")
public class EchoServlet extends HttpServlet {

	private static final long serialVersionUID = -1832065328420692123L;

	private static final Logger logger = LoggerFactory.getLogger(EchoServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Enumeration<String> headerNames = req.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String element = headerNames.nextElement();
			String header = req.getHeader(element);
			logger.info("Header: {} - {}", element, header);
		}

		Map<String, String[]> parameterMap = req.getParameterMap();
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			logger.info("parameter: {} - {}", entry.getKey(), entry.getValue());
		}

		resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
		resp.setContentType("application/json");
		PrintWriter writer = resp.getWriter();
		writer.write("ok");
	}

}
