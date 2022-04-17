package com.andyadc.bms.captcha;

import java.util.StringJoiner;

public class CaptchaDTO {

	private String captchaId;
	private String captchaCode;
	private String captchaPngImage;

	public String getCaptchaId() {
		return captchaId;
	}

	public void setCaptchaId(String captchaId) {
		this.captchaId = captchaId;
	}

	public String getCaptchaCode() {
		return captchaCode;
	}

	public void setCaptchaCode(String captchaCode) {
		this.captchaCode = captchaCode;
	}

	public String getCaptchaPngImage() {
		return captchaPngImage;
	}

	public void setCaptchaPngImage(String captchaPngImage) {
		this.captchaPngImage = captchaPngImage;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", CaptchaDTO.class.getSimpleName() + "[", "]")
			.add("captchaId=" + captchaId)
			.add("captchaCode=" + captchaCode)
			.add("captchaPngImage=" + captchaPngImage)
			.toString();
	}
}
