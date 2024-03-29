package com.andyadc.bms.security.model.token;

import com.andyadc.bms.security.model.Scopes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.List;
import java.util.Optional;

public class RefreshToken implements JwtToken {

	private static final long serialVersionUID = -87231197783753961L;

	private final Jws<Claims> claims;

	private RefreshToken(Jws<Claims> claims) {
		this.claims = claims;
	}

	/**
	 * Creates and validates Refresh token
	 */
	@SuppressWarnings("unchecked")
	public static Optional<RefreshToken> create(RawAccessJwtToken token, String signingKey) {
		Jws<Claims> claims = token.parseClaims(signingKey);

		List<String> scopes = claims.getPayload().get("scopes", List.class);
		if (scopes == null || scopes.isEmpty()
			|| !scopes.stream().filter(scope -> Scopes.REFRESH_TOKEN.authority().equals(scope)).findFirst().isPresent()) {
			return Optional.empty();
		}

		return Optional.of(new RefreshToken(claims));
	}

	@Override
	public String getToken() {
		return null;
	}

	public Jws<Claims> getClaims() {
		return claims;
	}

	public String getJti() {
		return claims.getPayload().getId();
	}

	public String getSubject() {
		return claims.getPayload().getSubject();
	}
}
