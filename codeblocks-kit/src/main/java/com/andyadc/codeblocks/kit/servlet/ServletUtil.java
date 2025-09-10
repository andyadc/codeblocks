package com.andyadc.codeblocks.kit.servlet;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public final class ServletUtil {

	private ServletUtil() {
	}

	public static void printHttpServletRequest(HttpServletRequest request) {
		System.out.println();
		System.out.println("/r ----------------------------- /r");
		System.out.println("Method : " + request.getMethod()); // GET
		System.out.println("PathInfo : " + request.getPathInfo()); // /bms
		System.out.println("PathTranslated : " + request.getPathTranslated()); // null
		System.out.println("ContextPath : " + request.getContextPath()); // /bms
		System.out.println("ServerName : " + request.getServerName()); // localhost
		System.out.println("ServletPath : " + request.getServletPath()); // /exception/throw
		System.out.println("ServerPort : " + request.getServerPort()); // 8080
		System.out.println("ServletContext : " + request.getServletPath()); // localhost
		System.out.println("RequestURI : " + request.getRequestURI()); // /exception/throw
		System.out.println("RequestURL : " + request.getRequestURL()); // /exception/throw
		System.out.println("QueryString : " + request.getQueryString()); // bustCache=0.48901322528370006
		System.out.println("AuthType : " + request.getAuthType()); // null
		System.out.println("LocalAddr : " + request.getLocalAddr()); // 0:0:0:0:0:0:0:1
		System.out.println("RemoteUser : " + request.getRemoteUser()); // null
		System.out.println("RemoteAddr : " + request.getRemoteAddr()); // 0:0:0:0:0:0:0:1
		System.out.println("RemoteHost : " + request.getRemoteHost()); // 0:0:0:0:0:0:0:1
		System.out.println("RemotePort : " + request.getRemotePort()); // 52631
		System.out.println("RequestedSessionId : " + request.getRequestedSessionId()); // null
		System.out.println("Scheme : " + request.getScheme()); // http
		System.out.println("Protocol : " + request.getProtocol()); // HTTP/1.1
		System.out.println("ContentType : " + request.getContentType()); //
		System.out.println("ContentLength : " + request.getContentLength()); //
		System.out.println("ContentLengthLong : " + request.getContentLengthLong()); //
		System.out.println("RequestedSessionId : " + request.getRequestedSessionId()); //
		System.out.println("/ ----------------------------- /");
		System.out.println();
	}

	public static Map<String, String> getHeaders(HttpServletRequest request) {
		Enumeration<String> headerNames = request.getHeaderNames();
		Map<String, String> headers = new HashMap<>();
		while (headerNames.hasMoreElements()) {
			String element = headerNames.nextElement();
			headers.put(element, request.getHeader(element));
		}
		return headers;
	}

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

		try {
			ServletInputStream inputStream = request.getInputStream();
			String toString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			System.out.println(toString);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String entityId = request.getParameter("entityId");
		System.out.println(entityId);

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
