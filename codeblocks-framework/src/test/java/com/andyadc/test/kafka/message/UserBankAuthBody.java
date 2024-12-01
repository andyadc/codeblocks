package com.andyadc.test.kafka.message;

import java.time.LocalDateTime;

public class UserBankAuthBody {

	private Long userId;
	private String username;
	private String mobile;
	private String bankcardNo;
	private Boolean result;
	private LocalDateTime authTime;

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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getBankcardNo() {
		return bankcardNo;
	}

	public void setBankcardNo(String bankcardNo) {
		this.bankcardNo = bankcardNo;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public LocalDateTime getAuthTime() {
		return authTime;
	}

	public void setAuthTime(LocalDateTime authTime) {
		this.authTime = authTime;
	}

	@Override
	public String toString() {
		return "UserBankAuthBody{" +
			"userId=" + userId +
			", username=" + username +
			", mobile=" + mobile +
			", bankcardNo=" + bankcardNo +
			", result=" + result +
			", authTime=" + authTime +
			'}';
	}

}
