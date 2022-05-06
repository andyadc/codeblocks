package com.andyadc.bms.security.model;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.List;

public class UserContext implements Serializable {

	private static final long serialVersionUID = -5195196736469492576L;

	private final String username;
	private final long timestamp;
	private Long uid;
	private String token;
	private List<GrantedAuthority> authorities;

	public UserContext(String username) {
		this.username = username;
		this.timestamp = System.currentTimeMillis();
	}

	private UserContext(String username, List<GrantedAuthority> authorities) {
		this(username);
		this.authorities = authorities;
	}

	public static UserContext create(String username, List<GrantedAuthority> authorities) {
		if (StringUtils.isBlank(username)) {
			throw new IllegalArgumentException("Username is blank: " + username);
		}
		return new UserContext(username, authorities);
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<GrantedAuthority> getAuthorities() {
		return authorities;
	}
}
