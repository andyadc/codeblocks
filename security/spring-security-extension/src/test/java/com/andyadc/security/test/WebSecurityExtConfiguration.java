package com.andyadc.security.test;

import com.andyadc.security.extension.configuers.authentication.LoginFilterSecurityConfigurer;
import com.andyadc.security.extension.configuers.authentication.miniapp.MiniAppClient;
import com.andyadc.security.extension.configuers.authentication.miniapp.MiniAppRequest;
import com.andyadc.security.extension.configuers.authentication.miniapp.MiniAppSessionKeyCache;
import com.andyadc.security.extension.configuers.authentication.miniapp.MiniAppUserDetailsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Profile("test")
@EnableWebSecurity(debug = true)
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class WebSecurityExtConfiguration {

	private final JwtEncoder jwtEncoder;

	public WebSecurityExtConfiguration(JwtEncoder jwtEncoder) {
		this.jwtEncoder = jwtEncoder;
	}

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable()
			.authorizeRequests()
			.mvcMatchers("/foo/**")
			.access("hasAuthority('ROLE_USER')").anyRequest().authenticated()
			.and()
			// 默认表单登录
			.formLogin()
			.and()
			.apply(new LoginFilterSecurityConfigurer<>())
			// 验证码登录
			.captchaLogin(captchaLoginConfigurer ->
				// 验证码校验 1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
				captchaLoginConfigurer.captchaService(this::verifyCaptchaMock)
					// 根据手机号查询用户UserDetials  1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
					.captchaUserDetailsService(this::loadUserByPhoneMock)
					// 生成JWT 返回  1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
					.jwtTokenGenerator(this::tokenResponseMock)
			)
			// 小程序登录 同时支持多个小程序
			.miniAppLogin(miniAppLoginConfigurer ->
				// 实现小程序多租户
				// 根据请求携带的clientid 查询小程序的appid和secret 1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
				miniAppLoginConfigurer.miniAppClientService(this::miniAppClientMock)
					// 小程序用户 自动注册和检索  1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
					.miniAppUserDetailsService(new MiniAppUserDetailsServiceMock())
					// 小程序sessionkey缓存 过期时间应该小于微信官方文档的声明   1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
					.miniAppSessionKeyCache(new MiniAppSessionKeyCacheMock())
					// 生成JWT 返回  1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
					.jwtTokenGenerator(this::tokenResponseMock)
			)
		;
		return httpSecurity.build();
	}

	private MiniAppClient miniAppClientMock(String clientId) {
		MiniAppClient miniAppClient = new MiniAppClient();
		miniAppClient.setClientId(clientId);
		miniAppClient.setAppId("wx234234324");
		miniAppClient.setSecret("x34431dssf234442231432");
		return miniAppClient;
	}

	private boolean verifyCaptchaMock(String phone, String code) {
		return code.equals("1234");
	}

	private UserDetails loadUserByPhoneMock(String phone) throws UsernameNotFoundException {
		return  // 用户名
			User.withUsername(phone)
				// 密码
				.password("password")
				// 权限集
				.authorities("ROLE_USER", "ROLE_ADMIN").build();
	}

	public OAuth2AccessTokenResponse tokenResponseMock(UserDetails userDetails) {
		JwsHeader jwsHeader = JwsHeader.with(SignatureAlgorithm.RS256)
			.type("JWT")
			.build();

		Instant issuedAt = Clock.system(ZoneId.of("Asia/Shanghai")).instant();
		Set<String> scopes = userDetails.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.toSet());

		Instant expiresAt = issuedAt.plusSeconds(5 * 60);
		JwtClaimsSet claimsSet = JwtClaimsSet.builder()
			.issuer("https://felord.cn")
			.subject(userDetails.getUsername())
			.expiresAt(expiresAt)
			.audience(Arrays.asList("client1", "client2"))
			.issuedAt(issuedAt)
			.claim("scope", scopes)
			.build();

		Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claimsSet));
		return OAuth2AccessTokenResponse.withToken(jwt.getTokenValue())
			.tokenType(OAuth2AccessToken.TokenType.BEARER)
			.expiresIn(expiresAt.getEpochSecond())
			.scopes(scopes)
			.refreshToken(UUID.randomUUID().toString())
			.build();
	}

	// *************************** mock ********************************
	static class MiniAppSessionKeyCacheMock implements MiniAppSessionKeyCache {

		@Override
		public String put(String cacheKey, String sessionKey) {
			return sessionKey;
		}

		@Override
		public String get(String cacheKey) {
			return "xxxxxxxxxx";
		}
	}

	static class MiniAppUserDetailsServiceMock implements MiniAppUserDetailsService {
		@Override
		public UserDetails register(MiniAppRequest request) {
			return // 用户名
				User.withUsername(request.getOpenId())
					// 密码
					.password("password")
					// 权限集
					.authorities("ROLE_USER", "ROLE_ADMIN").build();
		}

		@Override
		public UserDetails loadByOpenId(String clientId, String openId) {
			return // 用户名
				User.withUsername(openId)
					// 密码
					.password("password")
					// 权限集
					.authorities("ROLE_USER", "ROLE_ADMIN").build();
		}
	}
}
