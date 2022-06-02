package com.andyadc.security.extension2;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface ChannelUserDetailsService extends UserDetailsService {
	UserDetails loadByMobile(String mobile);
}
