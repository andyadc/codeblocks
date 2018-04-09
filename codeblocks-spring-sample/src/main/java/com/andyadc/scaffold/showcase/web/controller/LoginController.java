package com.andyadc.scaffold.showcase.web.controller;

import com.andyadc.scaffold.showcase.auth.security.CaptchaFormAuthenticationFilter;
import com.andyadc.scaffold.showcase.common.annotation.Performance;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @author andaicheng
 */
//@RequestMapping("/admin")
@Controller
public class LoginController {

    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

    private static final String PAGE_LOGIN = "login";
    private static final String PAGE_INDEX = "index";
    private static final String ATTR_MSG = "message";

    @GetMapping(value = "/login")
    public String login() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null && subject.isAuthenticated()) {
            subject.logout();
        }
        return PAGE_LOGIN;
    }

    @Performance
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, Model model) {
        String error_exception = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        LOG.info("login error_exception: {}", error_exception);

        if (StringUtils.isNotBlank(error_exception)) {
            if (CaptchaFormAuthenticationFilter.CaptchaValidationException.class.getName().equals(error_exception)) {
                model.addAttribute(ATTR_MSG, "验证码错误!");
                return PAGE_LOGIN;
            }
            if (IncorrectCredentialsException.class.getName().equals(error_exception)) {
                model.addAttribute(ATTR_MSG, "用户名或密码错误!");
                return PAGE_LOGIN;
            }
            if (UnknownAccountException.class.getName().equals(error_exception)) {
                model.addAttribute(ATTR_MSG, "用户名或密码错误!");
                return PAGE_LOGIN;
            }
            if (LockedAccountException.class.getName().equals(error_exception)) {
                model.addAttribute(ATTR_MSG, "账户被锁定!");
                return PAGE_LOGIN;
            }
            if (ExcessiveAttemptsException.class.getName().equals(error_exception)) {
                model.addAttribute(ATTR_MSG, "请稍后尝试登录!");
                return PAGE_LOGIN;
            }
        }
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index() {
        return PAGE_INDEX;
    }
}
