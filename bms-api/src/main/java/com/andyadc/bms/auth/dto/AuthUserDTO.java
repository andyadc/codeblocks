package com.andyadc.bms.auth.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;
import java.util.StringJoiner;

public class AuthUserDTO implements Serializable {

	private static final long serialVersionUID = -6139854448582320531L;

	private Long id;

	@Length(min = 3, max = 15, message = "用户名长度必须在3到15位之间")
	@NotBlank
	private String username;

	@Length(min = 6, max = 18, message = "密码长度必须在6到18位之间")
	@NotBlank
	private String password;
	private String confirmPassword;

	private Integer status;

	private List<String> authorities;

	public AuthUserDTO() {
	}

	public AuthUserDTO(Long id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
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

	@Override
	public String toString() {
		return new StringJoiner(", ", AuthUserDTO.class.getSimpleName() + "[", "]")
			.add("id=" + id)
			.add("username=" + username)
			.add("status=" + status)
			.add("authorities=" + authorities)
			.toString();
	}
}
