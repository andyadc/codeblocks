package com.andyadc.codeblocks.util.net;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author andaicheng
 * @version 2017/3/12
 */
public final class ServletUtils {

    private ServletUtils() {
    }

    /**
     * 获取所有请求参数
     */
    public static Map<String, String> getReqParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>(32);
        if (request == null) {
            return params;
        }
        Enumeration<?> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
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

    public static void setToCookie(HttpServletResponse response,
                                   String key, String value,
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
