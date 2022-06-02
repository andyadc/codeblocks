package com.andyadc.security.extension2.jwt;

import org.springframework.security.oauth2.jwt.Jwt;

@FunctionalInterface
public interface JwtEncoder {

	Jwt encode(JoseHeader header, JwtClaimsSet claims);
}
