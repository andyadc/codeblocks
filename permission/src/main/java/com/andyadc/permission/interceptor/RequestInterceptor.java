package com.andyadc.permission.interceptor;

import com.andyadc.codeblocks.kit.idgen.IDGen;
import com.andyadc.permission.context.RequestHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO
 *
 * @author andy.an
 * @since 2018/6/4
 */
public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);
    private final static String TRACE_ID = "traceId";
    private final static String HEADER_TRACE_ID = "X-TRACE-ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long now = System.currentTimeMillis();

        String traceId = request.getHeader(HEADER_TRACE_ID);
        if (traceId == null || traceId.trim().isEmpty()) {
            traceId = IDGen.uuid();
        }
        MDC.put(TRACE_ID, traceId);

        RequestHolder.add(request);
        RequestHolder.add(now);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        logger.info("Request elapsed: " + (System.currentTimeMillis() - RequestHolder.getReqBeginTime()) + " ms");
        MDC.remove(TRACE_ID);

        RequestHolder.remove();
    }
}
