package com.andyadc.bms.security.model;

public enum Scopes {
	REFRESH_TOKEN;

	public String authority() {
		return "ROLE_" + this.name();
	}
}
