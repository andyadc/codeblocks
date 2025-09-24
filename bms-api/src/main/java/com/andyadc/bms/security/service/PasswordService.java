package com.andyadc.bms.security.service;

import jakarta.inject.Inject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

	private final PasswordEncoder passwordEncoder;

	@Inject
	public PasswordService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * <pre>
	 * $2a$10$LB/IDOMJ8lb.foNZe5Jz6eu02tRSqyYMu.wXahGoNp83cc.BrVuuS
	 * 1.The “2a” represents the BCrypt algorithm version
	 * 2.The “10” represents the strength of the algorithm
	 * </pre>
	 */
	public String encode(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}
}
