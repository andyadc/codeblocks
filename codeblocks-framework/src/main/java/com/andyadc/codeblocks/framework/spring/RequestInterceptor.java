package com.andyadc.codeblocks.framework.spring;

import com.andyadc.codeblocks.kit.idgen.IDGen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author andy.an
 * @since 2018/6/4
 */
public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

    private final static String HTTP_HEADER_TRACE_ID = "X-TraceId";
    private final static String TRACE_ID = "traceId";
    private static ThreadLocal<Long> requestTimeCounterThreadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        requestTimeCounterThreadLocal.set(System.currentTimeMillis());

        String traceId = request.getHeader(HTTP_HEADER_TRACE_ID);
        if (traceId == null || traceId.isEmpty()) {
            traceId = IDGen.uuid();
        }
        MDC.put(TRACE_ID, traceId);
        logger.info(">>> " + request.getRequestURI());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        logger.info("Request elapsed: " + (System.currentTimeMillis() - requestTimeCounterThreadLocal.get()));
        MDC.remove(TRACE_ID);
        requestTimeCounterThreadLocal.remove();
    }
}
