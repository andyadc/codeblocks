package com.andyadc.bms.web;

import com.andyadc.bms.auth.dto.AuthUserDTO;
import com.andyadc.bms.auth.entity.AuthUser;
import com.andyadc.bms.auth.service.AuthUserService;
import com.andyadc.bms.event.OnUserRegistrationCompleteEvent;
import com.andyadc.bms.security.SecurityUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/api")
public class AuthUserController {

	private AuthUserService authUserService;
	private ApplicationEventPublisher applicationEventPublisher;

	@Inject
	public void setAuthUserService(AuthUserService authUserService) {
		this.authUserService = authUserService;
	}

	@Inject
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@PostMapping("/auth/register")
	public ResponseEntity<Object> register(@Validated @RequestBody AuthUserDTO user) {
		AuthUser registeredUser = authUserService.register(user);
		applicationEventPublisher.publishEvent(new OnUserRegistrationCompleteEvent(registeredUser));
		return ResponseEntity.ok("success");
	}

	@GetMapping("/me")
	public Object get() {
		SecurityUtils.getCurrentUsername().map(name -> {
			System.out.println(name);
			return name;
		});

		return SecurityContextHolder.getContext().getAuthentication();
	}

	@PreAuthorize("hasAuthority('user:add')")
	@GetMapping("/user/add")
	public ResponseEntity<Object> add() {
		return ResponseEntity.ok("Add user success");
	}

	@PreAuthorize("hasAuthority('user:del')")
	@GetMapping("/user/del")
	public ResponseEntity<Object> del() {
		return ResponseEntity.ok("Del user success");
	}

	@PreAuthorize("hasAuthority('user:update')")
	@GetMapping("/user/update")
	public ResponseEntity<Object> update() {
		return ResponseEntity.ok("Update user success");
	}
}
