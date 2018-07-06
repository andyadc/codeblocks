package com.andyadc.codeblocks.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author andy.an
 * @since 2018/7/6
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/login")
    public String toLogin() {

        return "admin/login";
    }

    @PostMapping("/login")
    public Object login() {

        return "";
    }
}
