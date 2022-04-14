package com.andyadc.bms.security.location;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Component;

@Component
public class DifferentLocationChecker implements UserDetailsChecker {

	@Override
	public void check(UserDetails toCheck) {

	}
}
