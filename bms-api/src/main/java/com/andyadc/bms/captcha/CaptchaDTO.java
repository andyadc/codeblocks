package com.andyadc.bms.captcha;

import java.util.StringJoiner;

public class CaptchaDTO {

	private Integer length;
	private Integer height;
	private Integer width;
	private String captchaId;
	private String captchaCode;
	private String captchaPngImage;

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

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
