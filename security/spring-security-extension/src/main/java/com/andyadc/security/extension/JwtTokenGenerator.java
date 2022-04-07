package com.andyadc.security.extension;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

public interface JwtTokenGenerator {

	OAuth2AccessTokenResponse token(UserDetails userDetails);
}
