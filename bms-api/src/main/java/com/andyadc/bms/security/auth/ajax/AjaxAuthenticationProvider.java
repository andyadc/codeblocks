package com.andyadc.bms.security.auth.ajax;

import com.andyadc.bms.auth.dto.AuthUserDTO;
import com.andyadc.bms.security.SecurityService;
import com.andyadc.bms.security.model.UserContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AjaxAuthenticationProvider implements AuthenticationProvider {

	private final PasswordEncoder passwordEncoder;
	private final SecurityService securityService;

	@Inject
	public AjaxAuthenticationProvider(PasswordEncoder passwordEncoder, SecurityService securityService) {
		this.passwordEncoder = passwordEncoder;
		this.securityService = securityService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.notNull(authentication, "No authentication data provided");

		String username = (String) authentication.getPrincipal();
		String password = (String) authentication.getCredentials();

		AuthUserDTO userDTO = securityService.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

		if (!passwordEncoder.matches(password, userDTO.getPassword())) {
			throw new BadCredentialsException("Authentication failed. Username or Password not valid.");
		}

		if (userDTO.getAuthorities() == null) {
			throw new InsufficientAuthenticationException("User has no authorities assigned");
		}

		List<GrantedAuthority> grantedAuthorities
			= userDTO.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

		UserContext context = UserContext.create(username, grantedAuthorities);
		context.setUid(userDTO.getId());
		return new UsernamePasswordAuthenticationToken(context, null, grantedAuthorities);
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(clazz));
	}
}
