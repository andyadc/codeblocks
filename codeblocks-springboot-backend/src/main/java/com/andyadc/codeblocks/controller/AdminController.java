package com.andyadc.codeblocks.controller;

import com.andyadc.codeblocks.kit.http.ServletUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

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
    public Object login(HttpServletRequest request, Model model) {
        System.out.println(ServletUtil.getReqParams(request));
        model.addAttribute("message", "success");
        return "admin/login";
    }
}
