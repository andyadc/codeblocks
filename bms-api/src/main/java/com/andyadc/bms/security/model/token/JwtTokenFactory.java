package com.andyadc.bms.security.model.token;

import com.andyadc.bms.security.configurers.JwtSettings;
import com.andyadc.bms.security.exception.JwtGenException;
import com.andyadc.bms.security.model.Scopes;
import com.andyadc.bms.security.model.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtTokenFactory implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenFactory.class);

	private final JwtSettings settings;
	private Key key;

	@Inject
	public JwtTokenFactory(JwtSettings settings) {
		this.settings = settings;
	}

	@Override
	public void afterPropertiesSet() {
		byte[] keyBytes = settings.getTokenSigningKey().getBytes();
		key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
	}

	public void invalidateJwtToken(UserContext userContext) {

	}

	/**
	 * Factory method for issuing new JWT Tokens.
	 */
	public AccessJwtToken createAccessJwtToken(UserContext userContext) {
		String username = userContext.getUsername();
		if (StringUtils.isBlank(username)) {
			throw new JwtGenException("Cannot create JWT Token without username");
		}

		if (userContext.getAuthorities() == null || userContext.getAuthorities().isEmpty()) {
			logger.warn("{} has no any privileges", username);
//			throw new JwtGenException("User doesn't have any privileges");
		}

		Claims claims = Jwts.claims().subject(username).build();
		claims.put("scopes", userContext.getAuthorities().stream().map(Object::toString).collect(Collectors.toList()));

		LocalDateTime currentTime = LocalDateTime.now();

//        Key key = new SecretKeySpec(settings.getTokenSigningKey().getBytes(), SignatureAlgorithm.HS512.getJcaName());

		String token = Jwts.builder()
			.claims(claims)
			.issuer(settings.getTokenIssuer())
			.issuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
			.expiration(Date.from(
				currentTime.plusMinutes(settings.getTokenExpirationTime())
					.atZone(ZoneId.systemDefault()).toInstant())
			)
//                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
			.signWith(key)
			.compact();

		return new AccessJwtToken(token, claims);
	}

	public JwtToken createRefreshToken(UserContext userContext) {
		if (StringUtils.isBlank(userContext.getUsername())) {
			throw new JwtGenException("Cannot create JWT Token without username");
		}

		LocalDateTime currentTime = LocalDateTime.now();

		Claims claims = Jwts.claims().subject(userContext.getUsername()).build();
		claims.put("scopes", Collections.singletonList(Scopes.REFRESH_TOKEN.authority()));

		String token = Jwts.builder()
			.claims(claims)
			.issuer(settings.getTokenIssuer())
			.id(UUID.randomUUID().toString())
			.issuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
			.expiration(Date.from(currentTime
				.plusMinutes(settings.getRefreshTokenExpTime())
				.atZone(ZoneId.systemDefault()).toInstant()))
//                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
			.signWith(key)
			.compact();

		return new AccessJwtToken(token, claims);
	}
}
