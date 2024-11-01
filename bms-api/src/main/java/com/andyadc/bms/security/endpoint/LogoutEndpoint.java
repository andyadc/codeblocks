package com.andyadc.bms.security.endpoint;

import com.andyadc.bms.common.Response;
import com.andyadc.bms.security.SecurityConstants;
import com.andyadc.bms.security.auth.jwt.extractor.TokenExtractor;
import com.andyadc.bms.security.configurers.JwtSettings;
import com.andyadc.bms.security.model.UserContext;
import com.andyadc.bms.security.model.token.RawAccessJwtToken;
import com.andyadc.bms.service.AuthTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

// TODO
@RestController
public class LogoutEndpoint {

	private TokenExtractor tokenExtractor;
	private JwtSettings jwtSettings;
	private AuthTokenService authTokenService;

	@Inject
	public void setTokenExtractor(TokenExtractor tokenExtractor) {
		this.tokenExtractor = tokenExtractor;
	}

	@Inject
	public void setJwtSettings(JwtSettings jwtSettings) {
		this.jwtSettings = jwtSettings;
	}

	@Inject
	public void setAuthTokenService(AuthTokenService authTokenService) {
		this.authTokenService = authTokenService;
	}

	@RequestMapping(
		value = "/api/auth/logout",
		method = {RequestMethod.GET, RequestMethod.POST},
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	public ResponseEntity<Object> logout(HttpServletRequest request) {
		String token = tokenExtractor.extract(request.getHeader(SecurityConstants.AUTHENTICATION_HEADER_NAME));
		RawAccessJwtToken rawToken = new RawAccessJwtToken(token);
		Jws<Claims> jwsClaims = rawToken.parseClaims(jwtSettings.getTokenSigningKey());
		String subject = jwsClaims.getBody().getSubject();

		authTokenService.removeAuthToken(new UserContext(subject));
		return ResponseEntity.ok(Response.success());
	}
}
