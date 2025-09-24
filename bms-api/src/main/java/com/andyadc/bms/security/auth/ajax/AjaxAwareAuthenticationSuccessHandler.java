package com.andyadc.bms.security.auth.ajax;

import com.andyadc.bms.common.RespCode;
import com.andyadc.bms.common.Response;
import com.andyadc.bms.security.SecurityConstants;
import com.andyadc.bms.security.model.UserContext;
import com.andyadc.bms.security.model.token.JwtToken;
import com.andyadc.bms.security.model.token.JwtTokenFactory;
import com.andyadc.bms.service.AuthTokenService;
import com.andyadc.codeblocks.kit.idgen.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AjaxAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final ObjectMapper mapper;
	private final JwtTokenFactory tokenFactory;

	private AuthTokenService authTokenService;

	@Inject
	public AjaxAwareAuthenticationSuccessHandler(final ObjectMapper mapper, final JwtTokenFactory tokenFactory) {
		this.mapper = mapper;
		this.tokenFactory = tokenFactory;
	}

	@Inject
	public void setAuthTokenService(AuthTokenService authTokenService) {
		this.authTokenService = authTokenService;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		UserContext userContext = (UserContext) authentication.getPrincipal();

		JwtToken accessToken = tokenFactory.createAccessJwtToken(userContext);
		JwtToken refreshToken = tokenFactory.createRefreshToken(userContext);

		Map<String, String> tokenMap = new HashMap<>(4, 1);
		tokenMap.put("token", accessToken.getToken());
		tokenMap.put("refreshToken", refreshToken.getToken());

		userContext.setToken(UUID.randomUUID());
		tokenMap.put("accessToken", userContext.getToken());

		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(SecurityConstants.DEFAULT_CHARACTER_ENCODING);
		mapper.writeValue(response.getWriter(), Response.of(RespCode.SUCC, tokenMap));

		clearAuthenticationAttributes(request);
		saveAuthenticationUserContext(userContext);
	}

	private void saveAuthenticationUserContext(UserContext userContext) {
		authTokenService.saveAuthToken(userContext);
	}

	/**
	 * Removes temporary authentication-related data which may have been stored
	 * in the session during the authentication process..
	 */
	protected final void clearAuthenticationAttributes(HttpServletRequest request) {
		final HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
}
