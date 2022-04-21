package com.andyadc.codeblocks.kit.servlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public final class ServletUtil {

	private ServletUtil() {
	}

	/**
	 * 		System.out.println(request.getContextPath()); // /bms
	 * 		System.out.println(request.getServletPath()); // /exception/throw
	 * 		System.out.println(request.getServerPort()); // 8080
	 * 		System.out.println(request.getServerName()); // localhost
	 * 		System.out.println(request.getScheme()); // http
	 * 		System.out.println(request.getRequestURI()); // /exception/throw
	 * 		System.out.println(request.getQueryString()); // bustCache=0.48901322528370006
	 * 		System.out.println(request.getRemoteHost()); // 0:0:0:0:0:0:0:1
	 * 		System.out.println(request.getMethod()); // GET
	 * 		System.out.println(request.getAuthType()); // null
	 * 		System.out.println(request.getLocalAddr()); // 0:0:0:0:0:0:0:1
	 * 		System.out.println(request.getRemoteAddr()); // 0:0:0:0:0:0:0:1
	 * 		System.out.println(request.getRemotePort()); // 52631
	 * 		System.out.println(request.getRequestedSessionId()); // null
	 * 		System.out.println(request.getRemoteUser()); // null
	 * 		System.out.println(request.getPathTranslated()); // null
	 * 		System.out.println(request.getProtocol()); // HTTP/1.1
	 */

	/**
	 * 获取所有请求参数
	 */
	public static Map<String, String> getReqParams(HttpServletRequest request) {
		Map<String, String> params = new HashMap<>(32);
		if (request == null) {
			return params;
		}
		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			String[] values = request.getParameterValues(paramName);
			if (values != null && values.length > 0) {
				params.put(paramName, values[0]);
			}
		}
		return params;
	}

	public static String getFromCookie(HttpServletRequest request,
									   String key) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(key)) {
				return cookie.getValue();
			}
		}
		return null;
	}

	public static void removeFromCookie(HttpServletRequest request,
										HttpServletResponse response,
										String key) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(key)) {
				cookie.setValue(null);
				cookie.setMaxAge(0);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}
	}

	public static void setToCookie(HttpServletResponse response,
								   String key,
								   String value,
								   int maxAge) {
		Cookie cookie = new Cookie(key, value);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		if (maxAge > 0) {
			cookie.setMaxAge(maxAge);
		}
		response.addCookie(cookie);
	}
}
