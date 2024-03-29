package com.andyadc.codeblocks.showcase.web.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class ControllerExceptionResolver implements HandlerExceptionResolver {

	private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		logger.error("Process url [{}] failed", request.getRequestURL().toString(), ex);
		ModelAndView view = new ModelAndView();
		Map<String, Object> data = new HashMap<>(8);
		data.put("code", "500");
		data.put("message", ex.getMessage());

		view.addAllObjects(data);
		return view;
	}
}
