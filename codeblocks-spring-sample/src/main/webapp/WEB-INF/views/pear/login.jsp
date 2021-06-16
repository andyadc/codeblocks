<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="path" value="${ctx}/static/pear"/>
<html>
<head>
	<meta charset="utf-8">
	<title>Login</title>
	<link href="${path}/admin/images/favicon.ico" rel="icon">
	<link rel="stylesheet" href="${ctx}/static/pear/component/pear/css/pear.css"/>
	<link rel="stylesheet" href="${ctx}/static/pear/admin/css/other/login.css"/>
</head>
<body style="background-size: cover;">
<form class="layui-form" action="javascript:void(0);">
	<div class="layui-form-item">
		<img class="logo" src="${ctx}/static/pear/admin/images/logo.png"/>
		<div class="title">Pear Admin</div>
		<div class="desc">
			~~~~~~~~~~~~~~~~~~~~
		</div>
	</div>
	<div class="layui-form-item">
		<input placeholder="账 户 : admin " lay-verify="required" hover class="layui-input"/>
	</div>
	<div class="layui-form-item">
		<input placeholder="密 码 : admin " lay-verify="required" hover class="layui-input"/>
	</div>
	<div class="layui-form-item">
		<input placeholder="验证码 : " hover lay-verify="required" class="code layui-input layui-input-inline"/>
		<img src="${ctx}/static/pear/admin/images/captcha.gif" class="codeImage"/>
	</div>
	<div class="layui-form-item">
		<input type="checkbox" name="" title="记住密码" lay-skin="primary" checked>
	</div>
	<div class="layui-form-item">
		<button type="button" class="pear-btn pear-btn-success login" lay-submit lay-filter="login">
			登 入
		</button>
	</div>
</form>
<script src="${ctx}/static/pear/component/layui/layui.js"></script>
<script src="${ctx}/static/pear/component/pear/pear.js"></script>
<script>
	layui.use(['form', 'button', 'popup'], function () {
		var form = layui.form;
		var button = layui.button;
		var popup = layui.popup;

		// 登 录 提 交
		form.on('submit(login)', function (data) {

			/// 验证

			/// 登录

			/// 动画
			button.load({
				elem: '.login',
				time: 1500,
				done: function () {
					popup.success("登录成功", function () {
						location.href = "index.html"
					});
				}
			})
			return false;
		});
	})
</script>
</body>
</html>
