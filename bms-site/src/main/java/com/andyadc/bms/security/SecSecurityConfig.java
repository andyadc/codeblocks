package com.andyadc.bms.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(
	prePostEnabled = true,
	securedEnabled = true,
	jsr250Enabled = true
)
public class SecSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final List<String> permitUrlList = new ArrayList<>();

	static {
		permitUrlList.add("/login");
		permitUrlList.add("/logout");
		permitUrlList.add("/signin");
		permitUrlList.add("/signup");
	}

	private UserDetailsService userDetailsService;
	private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	private AuthenticationFailureHandler authenticationFailureHandler;
	private LogoutSuccessHandler logoutSuccessHandler;
	private UserDetailsChecker userDetailsChecker;

	public SecSecurityConfig() {
		super();
	}

	@Inject
	@Named("secUserDetailsService")
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Inject
	public void setAuthenticationDetailsSource(AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource) {
		this.authenticationDetailsSource = authenticationDetailsSource;
	}

	@Inject
	public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler authenticationSuccessHandler) {
		this.authenticationSuccessHandler = authenticationSuccessHandler;
	}

	@Inject
	public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
		this.authenticationFailureHandler = authenticationFailureHandler;
	}

	@Inject
	public void setLogoutSuccessHandler(LogoutSuccessHandler logoutSuccessHandler) {
		this.logoutSuccessHandler = logoutSuccessHandler;
	}

	@Inject
	public void setUserDetailsChecker(UserDetailsChecker userDetailsChecker) {
		this.userDetailsChecker = userDetailsChecker;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.csrf().disable()

			.authorizeRequests()
				.antMatchers(permitUrlList.toArray(new String[0])).permitAll()
				.antMatchers("/invalidSession*").anonymous()
				.antMatchers("/user/updatePassword*").hasAuthority("CHANGE_PASSWORD_PRIVILEGE")
				.anyRequest().hasAuthority("READ_PRIVILEGE")

			.and()

			.sessionManagement()
				.invalidSessionUrl("/invalidSession.html")
				.maximumSessions(1).sessionRegistry(sessionRegistry())
				.and().sessionFixation().none()

			.and()

			.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/homepage.html")
				.failureUrl("/login?error=true")
				.successHandler(authenticationSuccessHandler)
				.failureHandler(authenticationFailureHandler)
				.authenticationDetailsSource(authenticationDetailsSource)
				.permitAll()

			.and()

			.logout()
				.logoutSuccessHandler(logoutSuccessHandler)
				.invalidateHttpSession(false)
				.logoutSuccessUrl("/logout.html?logSucc=true")
				.deleteCookies("JSESSIONID")
				.permitAll()

			.and()

			.rememberMe().rememberMeServices(null).key("ikey")

		;
		// @formatter:on
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.antMatchers("/resources/**")
			.antMatchers("/h2-console");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		SecAuthenticationProvider authenticationProvider = new SecAuthenticationProvider();
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPostAuthenticationChecks(userDetailsChecker);
		return authenticationProvider;
	}

	@Bean
	public RememberMeServices rememberMeServices() {
		return new SecRememberMeServices(
			"ikey", userDetailsService, new InMemoryTokenRepositoryImpl()
		);
	}

	@Bean
	public RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
		String hierarchy = "ROLE_ADMIN > ROLE_STAFF \n ROLE_STAFF > ROLE_USER";
		roleHierarchy.setHierarchy(hierarchy);
		return roleHierarchy;
	}

	@Bean
	public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
		DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
		expressionHandler.setRoleHierarchy(roleHierarchy());
		return expressionHandler;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(11);
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
