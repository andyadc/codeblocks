package com.andyadc.bms.security.configures;

import com.andyadc.bms.security.auth.ajax.AjaxAuthenticationProvider;
import com.andyadc.bms.security.auth.ajax.AjaxAwareAuthenticationFailureHandler;
import com.andyadc.bms.security.auth.ajax.AjaxAwareAuthenticationSuccessHandler;
import com.andyadc.bms.security.auth.ajax.AjaxLoginProcessingFilter;
import com.andyadc.bms.security.auth.jwt.JwtAuthenticationProvider;
import com.andyadc.bms.security.auth.jwt.JwtTokenAuthenticationProcessingFilter;
import com.andyadc.bms.security.auth.jwt.SkipPathRequestMatcher;
import com.andyadc.bms.security.auth.jwt.extractor.TokenExtractor;
import com.andyadc.bms.security.auth.mobile.MobileAuthenticationProvider;
import com.andyadc.bms.security.auth.mobile.MobileLoginAuthenticationProcessingFilter;
import com.andyadc.bms.security.handler.RestAuthenticationEntryPoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(
	prePostEnabled = true,
	securedEnabled = true,
	jsr250Enabled = true
)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String API_ROOT_URL = "/api/**";
	private static final String AUTHENTICATION_URL = "/api/auth/login";
	private static final String REFRESH_TOKEN_URL = "/api/auth/token";
	private static final String CAPTCHA_URL = "/api/captcha/**";
	private static final String REGISTER_URL = "/api/auth/register";
	private static final String LOGOUT_URL = "/api/auth/logout";

	private static final String MOBILE_LOGIN_URL = "/api/mobile/login";
	private static final String MOBILE_SEND_URL = "/api/mobile/send";

	private static final List<String> permitAllEndpointList = new ArrayList<>();

	static {
		permitAllEndpointList.add(AUTHENTICATION_URL);
		permitAllEndpointList.add(REFRESH_TOKEN_URL);
		permitAllEndpointList.add(REGISTER_URL);
		permitAllEndpointList.add(LOGOUT_URL);
		permitAllEndpointList.add(CAPTCHA_URL);

		permitAllEndpointList.add(MOBILE_LOGIN_URL);
		permitAllEndpointList.add(MOBILE_SEND_URL);

		permitAllEndpointList.add("/h2-console/**");
	}

	private RestAuthenticationEntryPoint authenticationEntryPoint;
	private AjaxAwareAuthenticationSuccessHandler authenticationSuccessHandler;
	private AjaxAwareAuthenticationFailureHandler authenticationFailureHandler;

	private AjaxAuthenticationProvider ajaxAuthenticationProvider;
	private JwtAuthenticationProvider jwtAuthenticationProvider;
	private MobileAuthenticationProvider mobileAuthenticationProvider;

	private AuthenticationManager authenticationManager;

	private TokenExtractor tokenExtractor;
	private ObjectMapper objectMapper;

	@Inject
	public void setAuthenticationEntryPoint(RestAuthenticationEntryPoint authenticationEntryPoint) {
		this.authenticationEntryPoint = authenticationEntryPoint;
	}

	@Inject
	public void setAuthenticationSuccessHandler(AjaxAwareAuthenticationSuccessHandler authenticationSuccessHandler) {
		this.authenticationSuccessHandler = authenticationSuccessHandler;
	}

	@Inject
	public void setAuthenticationFailureHandler(AjaxAwareAuthenticationFailureHandler authenticationFailureHandler) {
		this.authenticationFailureHandler = authenticationFailureHandler;
	}

	@Inject
	public void setJwtAuthenticationProvider(JwtAuthenticationProvider jwtAuthenticationProvider) {
		this.jwtAuthenticationProvider = jwtAuthenticationProvider;
	}

	@Inject
	public void setAjaxAuthenticationProvider(AjaxAuthenticationProvider ajaxAuthenticationProvider) {
		this.ajaxAuthenticationProvider = ajaxAuthenticationProvider;
	}

	@Inject
	public void setMobileAuthenticationProvider(MobileAuthenticationProvider mobileAuthenticationProvider) {
		this.mobileAuthenticationProvider = mobileAuthenticationProvider;
	}

	// TODO
	@Inject
	public void setAuthenticationManager(@Lazy AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Inject
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Inject
	public void setTokenExtractor(TokenExtractor tokenExtractor) {
		this.tokenExtractor = tokenExtractor;
	}

	protected AjaxLoginProcessingFilter buildAjaxLoginProcessingFilter(String loginEntryPoint) {
		AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter(loginEntryPoint, authenticationSuccessHandler, authenticationFailureHandler, objectMapper);
		filter.setAuthenticationManager(this.authenticationManager);
		return filter;
	}

	protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter(List<String> pathsToSkip, String pattern) {
		SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, pattern);
		JwtTokenAuthenticationProcessingFilter filter
			= new JwtTokenAuthenticationProcessingFilter(matcher, authenticationFailureHandler, tokenExtractor);
		filter.setAuthenticationManager(this.authenticationManager);
		return filter;
	}

	protected MobileLoginAuthenticationProcessingFilter buildMobileLoginAuthenticationProcessingFilter(String loginEntryPoint) {
		MobileLoginAuthenticationProcessingFilter filter = new MobileLoginAuthenticationProcessingFilter(loginEntryPoint, authenticationSuccessHandler, authenticationFailureHandler, objectMapper);
		filter.setAuthenticationManager(this.authenticationManager);
		return filter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.exceptionHandling()
			.authenticationEntryPoint(this.authenticationEntryPoint)

			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

			.and()
			.authorizeRequests()
			.antMatchers(permitAllEndpointList.toArray(new String[0]))
			.permitAll()

			.and()
			.authorizeRequests()
			.antMatchers(API_ROOT_URL)
			.authenticated()

			.and()
			.addFilterBefore(new CustomCorsFilter(), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(buildAjaxLoginProcessingFilter(AUTHENTICATION_URL), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(buildMobileLoginAuthenticationProcessingFilter(MOBILE_LOGIN_URL), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(permitAllEndpointList, API_ROOT_URL), UsernamePasswordAuthenticationFilter.class)
		;
	}

	@Override
	public void configure(WebSecurity web) {
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(ajaxAuthenticationProvider);
		auth.authenticationProvider(jwtAuthenticationProvider);
		auth.authenticationProvider(mobileAuthenticationProvider);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
