package com.andyadc.permission.common;

import com.andyadc.permission.exception.PermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author andaicheng
 * @since 2018/7/22
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String url = request.getRequestURL().toString();
        ModelAndView mv;

        // .json, .page
        Map<String, String> exceptionMap = new HashMap<>(8);
        exceptionMap.put("url", url);

        exceptionMap = buildExceptionMap(exceptionMap, ex, url);
        if (url.endsWith(".json")) {
            mv = new ModelAndView("jsonView", exceptionMap);
        } else if (url.endsWith(".page")) {
            mv = new ModelAndView("exception", exceptionMap);
        } else {
            logger.error("Unknown exception, url:" + url, ex);
            exceptionMap.put("code", "-1");
            exceptionMap.put("message", "system error");
            mv = new ModelAndView("jsonView", exceptionMap);
        }

        return mv;
    }

    private Map<String, String> buildExceptionMap(Map<String, String> exceptionMap, Exception ex, String url) {
        if (ex instanceof PermissionException) {
            exceptionMap.put("code", ((PermissionException) ex).getErrorCode());
            exceptionMap.put("message", ((PermissionException) ex).getErrorMessage());
        } else {
            logger.error("Unknown exception, url:" + url, ex);
            exceptionMap.put("code", "-1");
            exceptionMap.put("message", ex.getMessage());
        }
        return exceptionMap;
    }
}
