package com.andyadc.bms.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class PasswordService {

	private final PasswordEncoder passwordEncoder;

	@Inject
	public PasswordService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public String encode(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}
}
