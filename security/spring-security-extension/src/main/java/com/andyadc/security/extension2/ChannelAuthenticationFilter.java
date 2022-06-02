package com.andyadc.security.extension2;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ChannelAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private static final String CHANNEL_URI_VARIABLE_NAME = "channel";
	private static final RequestMatcher LOGIN_REQUEST_MATCHER
		= new AntPathRequestMatcher("/login/{" + CHANNEL_URI_VARIABLE_NAME + "}", "POST");

	private final List<LoginChannelProcessor<? extends AbstractAuthenticationToken>> channelFilters;

	public ChannelAuthenticationFilter(List<LoginChannelProcessor<? extends AbstractAuthenticationToken>> channelFilters) {
		super(LOGIN_REQUEST_MATCHER, authentication -> null);
		this.channelFilters = channelFilters;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		return null;
	}
}
