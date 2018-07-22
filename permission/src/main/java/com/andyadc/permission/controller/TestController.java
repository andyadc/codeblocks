package com.andyadc.permission.controller;

import com.andyadc.permission.exception.PermissionException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author andaicheng
 * @since 2018/7/22
 */
@Controller
public class TestController {

    @ResponseBody
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("ex.json")
    public void jsonException() {
        throw new PermissionException("404", "unkown json");
    }

    @GetMapping("ex.page")
    public void pageException() {
        throw new PermissionException("403", "unkown page");
    }
}
