package com.andyadc.security.extension2.jwt;

import org.springframework.security.oauth2.jwt.Jwt;

public class NimbusJwsEncoder implements JwtEncoder {

	@Override
	public Jwt encode(JoseHeader header, JwtClaimsSet claims) {
		return null;
	}
}
