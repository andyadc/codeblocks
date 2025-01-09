package com.andyadc.summer.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;

public class DispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = -8599482387681930006L;

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.info("{} {}", req.getMethod(), req.getRequestURI());
		PrintWriter writer = resp.getWriter();
		writer.write("<h1>Hello world!</h1>");
		writer.flush();
	}

}
