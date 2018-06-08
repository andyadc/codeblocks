package com.andyadc.codeblocks.showcase.web.controller;

import com.andyadc.codeblocks.showcase.auth.entity.AuthUser;
import com.andyadc.codeblocks.showcase.auth.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/user")
    public Object user() {
        AuthUser authUser = authService.findAuthUserByUsername("admin");
        return authUser;
    }

    @GetMapping("/exception")
    public Object exception() {
        throw new RuntimeException("no no no");
    }

}