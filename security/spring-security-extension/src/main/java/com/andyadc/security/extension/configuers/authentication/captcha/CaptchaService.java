package com.andyadc.security.extension.configuers.authentication.captcha;

public interface CaptchaService {

	boolean verifyCaptcha(String phone, String rawCode);
}
