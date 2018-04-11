<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>LOG IN</title>
    <link rel="icon" href="${ctx}/static/images/Thunder_bolt.ico">
    <link rel="stylesheet" href="${ctx}/static/css/login.css">
    <link rel="stylesheet" href="${ctx}/static/plugins/toastr/toastr.min.css">
</head>
<body>
<div id="header">
    <div class="header-layout">
        <h2 class="logo-title">
            ♔
            -
            LOGIN
        </h2>
        <ul class="header-nav">
            <li class="nav-first"><a href="${ctx}">首页</a></li>
            <li><a>注册</a></li>
        </ul>
    </div>
</div>
<div class="content">
    <div id="login-wrap" class="login-static">
        <input type="hidden" id="msg" value="${message}"/>
        <form id="login-form" name="login-form" method="post" action="${ctx}/login"
              class="form clr style-type-vertical lang-vertical">
            <input type="hidden" name="failTimes" value="${failTimes}"/>
            <div id="login-title" style="font-size: 12px; font-weight: normal;">
                ❤
            </div>
            <div id="login-content" class="form clr">
                <dl>
                    <dt class="fm-label">
                        <div class="fm-label-wrap clr">
                            <span id="login-id-label-extra" class="fm-label-extra"></span> <label
                                for="fm-login-id">账户:</label>
                        </div>
                    </dt>
                    <dd id="fm-login-id-wrap" class="fm-field">
                        <div class="fm-field-wrap">
                            <input id="fm-login-id" class="fm-text" name="username" tabindex="1" placeholder="账户"
                                   value="${auth_username_value}" autocomplete="off" autocorrect="off"
                                   autocapitalize="off"/>
                        </div>
                    </dd>
                </dl>
                <dl>
                    <dt class="fm-label">
                        <div class="fm-label-wrap clr">
								<span class="fm-label-extra"> <a id="forgot-password-link" href="#">
										忘记密码?
									</a>
								</span> <label for="fm-login-password">密码:</label>
                        </div>
                    </dt>
                    <dd id="fm-login-password-wrap" class="fm-field">
                        <div class="fm-field-wrap">
                            <input id="fm-login-password" class="fm-text" type="password" name="password" tabindex="2"
                                   autocomplete="off" placeholder="密码" autocorrect="off" autocapitalize="off"/>
                        </div>
                    </dd>
                </dl>
                <c:if test="${auth_captcha_required != null}">
                    <dl>
                        <dt class="fm-label">
                            <div class="fm-label-wrap clr">
                                <label for="fm-login-captcha">验证码:</label>
                            </div>
                        </dt>
                        <dd id="fm-login-captcha-wrap" class="fm-field">
                            <div class="fm-field-wrap">
                                <input id="fm-login-captcha" class="fm-text" type="text" name="captcha" tabindex="3"
                                       autocomplete="off" placeholder="验证码" autocorrect="off" autocapitalize="off"
                                       style="width: 100px; margin-right: 10px;"/>
                                <img style="cursor: pointer;margin-top: 5px;margin-left: 5px" alt="验证码"
                                     src="${ctx}/static/images/captcha_placeholder.jpg" title="看不清？点击刷新"
                                     id="img_captcha" onclick="refreshCaptcha();"/>
                            </div>
                        </dd>
                    </dl>
                </c:if>
            </div>
            <div id="login-submit">
                <input id="fm-login-submit" value="登录" class="fm-button fm-submit" type="submit" tabindex="3"
                       name="submit-btn"/>
            </div>
        </form>
    </div>
</div>

<script type="text/javascript" src="${ctx}/static/common/jquery/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="${ctx}/static/plugins/jquery-validation-1.17/jquery.validate.min.js"></script>
<script type="text/javascript" src="${ctx}/static/plugins/jquery-validation-1.17/localization/messages_zh.js"></script>
<script type="text/javascript" src="${ctx}/static/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript" src="${ctx}/static/js/login.js"></script>
<script type="text/javascript">
    var basePath = '${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/';//全路径

    /**
     * 当session超时的时候,会在当前页面刷新出登录界面,但是由于起初的设计原因和shiro的跳转策略问题,
     * 重新登陆后跳转的并不完美,则诞生如下代码,当弹出登陆界面的时候,刷新主页面,让整个系统跳转到登陆界面中, 登陆后则重新开始.
     */
    /*try {
     if (self.frameElement && self.frameElement.tagName == "IFRAME") {
     top.location = basePath;
     }
     } catch (e) {
     }*/
</script>
</body>
</html>