package com.andyadc.bms.security.configurers;

import com.andyadc.bms.security.Constants;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * CustomCorsFilter
 */
public class CustomCorsFilter extends CorsFilter {

	public CustomCorsFilter() {
		super(configurationSource());
	}

	private static UrlBasedCorsConfigurationSource configurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOriginPattern("*");
//        config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.setMaxAge(36000L);
		config.setAllowedMethods(
			Arrays.asList("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
		);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration(Constants.ROOT_API_URL, config);
		return source;
	}
}
