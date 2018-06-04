package com.andyadc.codeblocks.showcase.web.controller;

import com.andyadc.codeblocks.showcase.auth.entity.AuthUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author andy.an
 * @since 2018/6/4
 */
@RequestMapping("/public")
@RestController
public class PublicController {

    private static final Logger logger = LoggerFactory.getLogger(PublicController.class);

    @GetMapping("/user")
    public Object user() {
        AuthUser authUser = new AuthUser();
        authUser.setId(1L);
        authUser.setNickname("");
        authUser.setUpdatedTime(new Date());
        return authUser;
    }

}
