package com.andyadc.bms.security.extension.jwt;

import org.springframework.security.oauth2.jwt.Jwt;

@FunctionalInterface
public interface JwtEncoder {

	Jwt encode(JoseHeader header);
}
