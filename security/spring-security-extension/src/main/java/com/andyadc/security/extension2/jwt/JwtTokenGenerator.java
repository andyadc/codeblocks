package com.andyadc.security.extension2.jwt;

import com.andyadc.security.extension2.JwtProperties;
import com.andyadc.security.extension2.SecureUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtTokenGenerator {

	private final JwtProperties.Claims claims;
	private final JwtTokenStorage jwtTokenStorage;
	private final JwtEncoder jwtEncoder;

	public JwtTokenGenerator(JwtProperties.Claims claims, JwtTokenStorage jwtTokenStorage, JwtEncoder jwtEncoder) {
		this.claims = claims;
		this.jwtTokenStorage = jwtTokenStorage;
		this.jwtEncoder = jwtEncoder;
	}

	/**
	 * Token response o auth 2 access token response.
	 *
	 * @param userDetails the user details
	 * @return the o auth 2 access token response
	 */
	public BearerTokenResponse tokenResponse(SecureUser userDetails) {
		JoseHeader joseHeader = JoseHeader
			.withAlgorithm(SignatureAlgorithm.RS256)
			.type("JWT")
			.build();

		Instant issuedAt = Clock.system(ZoneId.of("Asia/Shanghai")).instant();

		Set<String> scopes = userDetails.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.toSet());

		Integer expiresAt = claims.getExpiresAt();

		String username = userDetails.getUsername();
		String userId = userDetails.getUserId();
		JwtClaimsSet sharedClaims = JwtClaimsSet.builder()
			// 签发者
			.issuer(claims.getIssuer())
			// 面向的用户群体
			.subject(claims.getSubject())
			// 接收jwt的一方 userid
			.audience(Collections.singletonList(username))
			.claim("scopes", scopes)
			.issuedAt(issuedAt)
			.claim("uid", userId)
			.build();

		Jwt accessToken = jwtEncoder.encode(joseHeader, JwtClaimsSet.from(sharedClaims)
			.expiresAt(issuedAt.plusSeconds(expiresAt))
			.build());
		Jwt refreshToken = jwtEncoder.encode(joseHeader, sharedClaims);

		OAuth2AccessTokenResponse tokenResponse = OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
			.tokenType(OAuth2AccessToken.TokenType.BEARER)
			.expiresIn(expiresAt)
			.scopes(scopes)
			.refreshToken(refreshToken.getTokenValue()).build();
		return jwtTokenStorage.put(tokenResponse, username);
	}
}
