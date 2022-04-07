package com.andyadc.security.extension.configuers.authentication.captcha;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CaptchaUserDetailsService {

	UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException;
}
