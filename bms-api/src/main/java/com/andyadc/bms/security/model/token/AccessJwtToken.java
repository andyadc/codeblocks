package com.andyadc.bms.security.model.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jsonwebtoken.Claims;

public class AccessJwtToken implements JwtToken {

	private static final long serialVersionUID = -4023563363373843261L;

	private final String rawToken;
	@JsonIgnore
	private final Claims claims;

	protected AccessJwtToken(final String token, Claims claims) {
		this.rawToken = token;
		this.claims = claims;
	}

	public Claims getClaims() {
		return claims;
	}

	@Override
	public String getToken() {
		return this.rawToken;
	}
}
