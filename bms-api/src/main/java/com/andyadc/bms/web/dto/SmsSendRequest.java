package com.andyadc.bms.web.dto;

import com.andyadc.bms.validation.PhoneNumber;

import javax.validation.constraints.NotBlank;

public class SmsSendRequest {

	@NotBlank(message = "手机号码不能为空")
	@PhoneNumber(regex = "^1[3-9](\\d){9}$", message = "手机号码非法")
	private String phoneNo;

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
}
