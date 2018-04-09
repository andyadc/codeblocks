package com.andyadc.scaffold.showcase.common.web.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author andaicheng
 * @version 2017/3/12
 */
public class ServletUtils {

    private ServletUtils() {
    }

    /**
     * 获取所有请求参数
     */
    public static Map<String, String> getReqParameters(HttpServletRequest request) {
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
}
