package com.andyadc.scaffold.showcase.auth.security;

import com.andyadc.codeblocks.captcha.servlet.CaptchaServlet;
import com.andyadc.codeblocks.util.StringUtils;
import com.andyadc.codeblocks.util.net.ServletUtils;
import com.andyadc.scaffold.showcase.auth.entity.AuthUser;
import com.andyadc.scaffold.showcase.auth.service.AuthService;
import com.andyadc.scaffold.showcase.common.cache.EhCacheHandler;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author andaicheng
 */
public class CaptchaFormAuthenticationFilter extends FormAuthenticationFilter {

    private static final int LOGIN_FAILURE_LIMIT = 2;
    private static final String CAPTACHE_PARAM = "captcha";

    /**
     * 达到验证失败次数限制，传递标志属性，登录界面显示验证码输入
     */
    private static final String KEY_AUTH_CAPTCHA_REQUIRED = "auth_captcha_required";

    /**
     * 记录用户输入的用户名信息，用于登录界面回显
     */
    private static final String KEY_AUTH_USERNAME_VALUE = "auth_username_value";

    /**
     * 登录失败缓存前缀
     */
    private static final String CACHE_LOGIN_FAIL_PREFIX = "login_fail_times_";

    private AuthService authService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {

        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) createToken(request, response);
        try {
            String username = getUsername(request);
            request.setAttribute(KEY_AUTH_USERNAME_VALUE, username);

            String failStr = request.getParameter("failTimes");
            int times = 0;
            if (StringUtils.isNotBlank(failStr)) {
                times = Integer.parseInt(failStr);
            }
            if (times == LOGIN_FAILURE_LIMIT) {
                request.setAttribute(KEY_AUTH_CAPTCHA_REQUIRED, Boolean.TRUE);
            }

            String captcha = request.getParameter(CAPTACHE_PARAM);
            if (times > LOGIN_FAILURE_LIMIT && StringUtils.isBlank(captcha)) {
                throw new CaptchaValidationException("请输入验证码");
            }

            if (StringUtils.isNotBlank(captcha) && !CaptchaServlet.validateCaptcha((HttpServletRequest) request, captcha)) {
                throw new CaptchaValidationException("验证码不正确");
            }

            AuthUser authUser = authService.findAuthUserByUsername(username);
            if (authUser != null) {
                String uk = ServletUtils.getFromCookie((HttpServletRequest) request, "captcha_flag");
                Integer loginTimes = (Integer) EhCacheHandler.get(CACHE_LOGIN_FAIL_PREFIX + uk);
                if (loginTimes != null && loginTimes >= LOGIN_FAILURE_LIMIT) {
                    captcha = request.getParameter(CAPTACHE_PARAM);
                    if (!CaptchaServlet.validateCaptcha((HttpServletRequest) request, captcha)) {
                        throw new CaptchaValidationException("验证码不正确");
                    }
                }
                Subject subject = getSubject(request, response);
                subject.login(usernamePasswordToken);
                return onLoginSuccess(usernamePasswordToken, subject, request, response);
            } else {
                request.setAttribute("failTimes", times + 1);
                return onLoginFailure(usernamePasswordToken, new UnknownAccountException("登录账号或密码不正确"), request, response);
            }
        } catch (AuthenticationException e) {
            return onLoginFailure(usernamePasswordToken, e, request, response);
        }
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();
        EhCacheHandler.remove(CACHE_LOGIN_FAIL_PREFIX + username);
        return super.onLoginSuccess(token, subject, request, response);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        if (e instanceof CaptchaValidationException) {
            request.setAttribute(KEY_AUTH_CAPTCHA_REQUIRED, Boolean.TRUE);
        } else if (e instanceof IncorrectCredentialsException | e instanceof UnknownAccountException) {
            e = new IncorrectCredentialsException("登录账号或密码不正确");

            UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
            String username = usernamePasswordToken.getUsername();

            String uk = ServletUtils.getFromCookie((HttpServletRequest) request, "captcha_flag");

            Integer loginTimes = (Integer) EhCacheHandler.get(CACHE_LOGIN_FAIL_PREFIX + uk);
            if (loginTimes != null && loginTimes >= LOGIN_FAILURE_LIMIT) {
                request.setAttribute(KEY_AUTH_CAPTCHA_REQUIRED, Boolean.TRUE);
            }

            if (loginTimes == null) {
                EhCacheHandler.put(CACHE_LOGIN_FAIL_PREFIX + uk, 1);
            } else {
                EhCacheHandler.put(CACHE_LOGIN_FAIL_PREFIX + uk, loginTimes + 1);
            }

        }
        return super.onLoginFailure(token, e, request, response);
    }

    private int getLoginFailureTimes(String username) {

        return 0;
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        return createToken(username, password, request, response);
    }

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    public static class CaptchaValidationException extends AuthenticationException {
        private static final long serialVersionUID = 5211956589460674220L;

        public CaptchaValidationException(String message) {
            super(message);
        }
    }
}
