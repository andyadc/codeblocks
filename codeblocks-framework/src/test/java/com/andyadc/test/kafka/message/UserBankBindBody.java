package com.andyadc.test.kafka.message;

import java.time.LocalDateTime;

public class UserBankBindBody {

	private Long userId;
	private String username;
	private String bankcardNo;
	private String token;
	private Boolean result;
	private LocalDateTime bindTime;

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

	public String getBankcardNo() {
		return bankcardNo;
	}

	public void setBankcardNo(String bankcardNo) {
		this.bankcardNo = bankcardNo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public LocalDateTime getBindTime() {
		return bindTime;
	}

	public void setBindTime(LocalDateTime bindTime) {
		this.bindTime = bindTime;
	}

	@Override
	public String toString() {
		return "UserBankBindBody{" +
			"userId=" + userId +
			", username=" + username +
			", bankcardNo=" + bankcardNo +
			", token=" + token +
			", result=" + result +
			", bindTime=" + bindTime +
			'}';
	}

}
