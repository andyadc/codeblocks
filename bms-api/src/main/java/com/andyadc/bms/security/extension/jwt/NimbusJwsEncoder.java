package com.andyadc.bms.security.extension.jwt;

import org.springframework.security.oauth2.jwt.Jwt;

public class NimbusJwsEncoder implements JwtEncoder {

	@Override
	public Jwt encode(JoseHeader header, JwtClaimsSet claims) {
		return null;
	}
}
