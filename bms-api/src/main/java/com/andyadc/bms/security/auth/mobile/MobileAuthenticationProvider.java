package com.andyadc.bms.security.auth.mobile;

import com.andyadc.bms.modules.auth.dto.AuthUserDTO;
import com.andyadc.bms.security.SecurityService;
import com.andyadc.bms.security.model.UserContext;
import com.andyadc.bms.service.MobileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MobileAuthenticationProvider implements AuthenticationProvider {

	private static final Logger logger = LoggerFactory.getLogger(MobileAuthenticationProvider.class);

	private SecurityService securityService;
	private MobileService mobileService;

	@Inject
	public void setSmsService(MobileService mobileService) {
		this.mobileService = mobileService;
	}

	@Inject
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.notNull(authentication, "No authentication data provided");

		MobileAuthenticationToken authenticationToken = (MobileAuthenticationToken) authentication;
		String phoneNo = (String) authenticationToken.getPrincipal();
		String verificationCode = (String) authenticationToken.getCredentials();

		boolean checked = mobileService.checkVerificationCode(phoneNo, verificationCode);
		if (!checked) {
			logger.warn("VerificationCode is error. {}", phoneNo);
			throw new BadCredentialsException("Authentication failed. PhoneNo or VerificationCode not valid.");
		}

		AuthUserDTO userDTO = securityService.findByPhoneNo(phoneNo)
			.orElseThrow(() -> new UsernameNotFoundException("User not found: " + phoneNo));

		if (userDTO.getAuthorities() == null) {
			logger.warn("user authorities is null.");
			throw new InsufficientAuthenticationException("User has no authorities assigned");
		}

		List<GrantedAuthority> grantedAuthorities
			= userDTO.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

		UserContext context = UserContext.create(userDTO.getUsername(), grantedAuthorities);
		context.setUid(userDTO.getId());

		return new MobileAuthenticationToken(context, verificationCode, grantedAuthorities);
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return MobileAuthenticationToken.class.isAssignableFrom(clazz);
	}
}
