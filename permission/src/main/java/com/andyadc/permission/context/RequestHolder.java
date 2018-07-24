package com.andyadc.permission.context;

import com.andyadc.permission.vo.UserVo;

import javax.servlet.http.HttpServletRequest;

/**
 * @author andy.an
 * @since 2018/7/24
 */
public class RequestHolder {

    private static final ThreadLocal<UserVo> USER_THREAD_LOCAL = new ThreadLocal<>();

    private static final ThreadLocal<HttpServletRequest> REQUEST_THREAD_LOCAL = new ThreadLocal<>();

    private static ThreadLocal<Long> REQUEST_TIME_BEGIN_THREAD_LOCAL = new ThreadLocal<>();

    public static void add(long reqBeginTime) {
        REQUEST_TIME_BEGIN_THREAD_LOCAL.set(reqBeginTime);
    }

    public static void add(UserVo vo) {
        USER_THREAD_LOCAL.set(vo);
    }

    public static void add(HttpServletRequest request) {
        REQUEST_THREAD_LOCAL.set(request);
    }

    public static long getReqBeginTime() {
        return REQUEST_TIME_BEGIN_THREAD_LOCAL.get();
    }

    public static UserVo getCurrentUser() {
        return USER_THREAD_LOCAL.get();
    }

    public static HttpServletRequest getCurrentRequest() {
        return REQUEST_THREAD_LOCAL.get();
    }

    public static void remove() {
        REQUEST_TIME_BEGIN_THREAD_LOCAL.remove();
        USER_THREAD_LOCAL.remove();
        REQUEST_THREAD_LOCAL.remove();
    }
}
