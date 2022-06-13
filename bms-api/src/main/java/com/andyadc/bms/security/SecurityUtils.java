package com.andyadc.bms.security;

import com.andyadc.bms.security.model.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class SecurityUtils {

	private static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);

	private SecurityUtils() {
	}

	public static Optional<String> getCurrentUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			logger.warn("No authentication in security context found.");
			return Optional.empty();
		}

		String username = null;
		Object target = authentication.getPrincipal();
		if (target instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) target;
			username = userDetails.getUsername();
		} else if (target instanceof UserContext) {
			UserContext userContext = (UserContext) target;
			username = userContext.getUsername();
		} else if (target instanceof String) {
			username = (String) target;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Found user [{}] in security context.", username);
		}
		return Optional.ofNullable(username);
	}

	public static Object getPrincipal() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null
			&& authentication.isAuthenticated()
			&& !(authentication instanceof AnonymousAuthenticationToken)) {
			return authentication.getPrincipal();
		}
		return null;
	}
}
