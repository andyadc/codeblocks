package com.andyadc.bms.modules.auth.dto;

import com.andyadc.bms.validation.PhoneNumber;
import com.andyadc.codeblocks.kit.mask.MaskType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class AuthUserDTO implements Serializable {

	private static final long serialVersionUID = -6139854448582320531L;

	private Long id;

	@Size(min = 3, max = 15, message = "用户名长度必须在3到15位之间")
	@NotBlank
	private String username;

	@Email
	private String email;

	@PhoneNumber(regex = "^1[3-9](\\d){9}$")
	private String phoneNo;

	@NotBlank
	private String password;
	private String confirmPassword;

	private Integer status;

	private String requestIP;

	private List<String> authorities = new ArrayList<>();

	public AuthUserDTO() {
	}

	public AuthUserDTO(Long id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
	}

	public boolean passwordMatch() {
		return password != null && password.equals(confirmPassword);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}

	public String getRequestIP() {
		return requestIP;
	}

	public void setRequestIP(String requestIP) {
		this.requestIP = requestIP;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", AuthUserDTO.class.getSimpleName() + "[", "]")
			.add("id=" + id)
			.add("username=" + username)
			.add("password=" + MaskType.PASSWORD.mask(password))
			.add("email=" + MaskType.EMAIL.mask(email))
			.add("phoneNo=" + MaskType.MOBILE.mask(phoneNo))
			.add("status=" + status)
			.add("requestIP=" + requestIP)
			.add("authorities=" + authorities)
			.toString();
	}
}
