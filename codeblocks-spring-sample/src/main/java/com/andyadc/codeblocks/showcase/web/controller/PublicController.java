package com.andyadc.codeblocks.showcase.web.controller;

import com.andyadc.codeblocks.common.Response;
import com.andyadc.codeblocks.showcase.auth.entity.AuthUser;
import com.andyadc.codeblocks.showcase.auth.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author andy.an
 * @since 2018/6/4
 */
@RequestMapping("/public")
@RestController
public class PublicController {

    private static final Logger logger = LoggerFactory.getLogger(PublicController.class);

    @Autowired
    private AuthService authService;

	@Qualifier(value = "taskExecutor")
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	@Qualifier(value = "defaultTaskExecutor")
	@Autowired
	private ThreadPoolTaskExecutor defaultTaskExecutor;

    @GetMapping("/user")
    public Object user() {
        AuthUser authUser = authService.findAuthUserByUsername("admin");
        return Response.success(authUser);
    }

    @GetMapping("/exception")
    public Object exception() {
        throw new RuntimeException("no no no");
    }

}
