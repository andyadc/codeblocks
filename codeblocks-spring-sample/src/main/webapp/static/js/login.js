$(function () {

    // 忘记密码
    $('#forgot-password-link').click(function () {
        toastr.options = {
            "closeButton": true,
            "progressBar": true,
            "positionClass": "toast-top-center",
            "timeOut": "3000"
        };
        toastr["info"]("请联系管理员重置密码");
    });

    // 默认焦点
    $('#fm-login-id').focus();

    // 表单验证
    $('#login-form').validate({
        rules: {
            username: {
                required: true
            },
            password: {
                required: true
            },
            captcha: {
                required: true
            }
        }
    });

    // 弹出服务器消息
    var msg = $('#msg').val();
    if (msg) {
        toastr.options = {
            "closeButton": true,
            //"progressBar" : true,
            "positionClass": "toast-top-center",
            "timeOut": "5000"
        };
        toastr["error"](msg);
    }

    $('#fm-login-captcha').on('click', function () {
        var src = $('#img_captcha').attr('src').substr(-3);
        if (src === 'jpg') {
            refreshCaptcha();
        }
    });

    if ($("#auth_captcha_required") != null) {
        refreshCaptcha();
    }
});

/**
 * 验证码刷新
 */
function refreshCaptcha() {
    document.getElementById("img_captcha").src = basePath + "public/captcha.servlet?t=" + Math.random();
}