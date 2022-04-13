package com.andyadc.bms.security.model;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.List;

public class UserContext implements Serializable {

	private static final long serialVersionUID = -1596997375272146695L;
	private final String username;
	private final long timestamp;
	private final List<GrantedAuthority> authorities;
	private Long uid;

	private UserContext(String username, List<GrantedAuthority> authorities) {
		this.username = username;
		this.authorities = authorities;
		this.timestamp = System.currentTimeMillis();
	}

	public static UserContext create(String username, List<GrantedAuthority> authorities) {
		if (StringUtils.isBlank(username)) throw new IllegalArgumentException("Username is blank: " + username);
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

	public List<GrantedAuthority> getAuthorities() {
		return authorities;
	}
}
