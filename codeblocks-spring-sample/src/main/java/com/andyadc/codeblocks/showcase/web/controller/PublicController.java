package com.andyadc.codeblocks.showcase.web.controller;

import com.andyadc.codeblocks.showcase.auth.entity.AuthUser;
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

    @GetMapping("/user")
    public Object user() {
        AuthUser authUser = new AuthUser();
        authUser.setId(1L);
        return authUser;
    }

}
