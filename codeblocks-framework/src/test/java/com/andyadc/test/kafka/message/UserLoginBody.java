package com.andyadc.test.kafka.message;

import java.time.LocalDateTime;

public class UserLoginBody {

	private Long userId;
	private String username;
	private Boolean result;
	private LocalDateTime loginTime;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public LocalDateTime getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(LocalDateTime loginTime) {
		this.loginTime = loginTime;
	}

	@Override
	public String toString() {
		return "UserLoginBody{" +
			"userId=" + userId +
			", username=" + username +
			", result=" + result +
			", loginTime=" + loginTime +
			'}';
	}

}
