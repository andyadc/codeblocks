package com.andyadc.test.kafka.message;

import java.time.LocalDateTime;

public class UserRegisterBody {

	private String username;
	private String mobile;
	private String email;
	private String ip;
	private Boolean result;
	private LocalDateTime registerTime;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public LocalDateTime getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(LocalDateTime registerTime) {
		this.registerTime = registerTime;
	}

	@Override
	public String toString() {
		return "UserRegisterBody{" +
			"username=" + username +
			", mobile=" + mobile +
			", email=" + email +
			", ip=" + ip +
			", result=" + result +
			", registerTime=" + registerTime +
			'}';
	}

}
