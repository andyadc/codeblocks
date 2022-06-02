package com.andyadc.security.extension2;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * The interface Http security customizer.
 */
@FunctionalInterface
public interface HttpSecurityCustomizer {

	/**
	 * Customize.
	 */
	void customize(HttpSecurity http);
}
