package com.andyadc.codeblocks.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author andy.an
 * @since 2018/5/8
 */
@Controller
public class IndexController {

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("welcome", "andaicheng");
        return "index";
    }

    @GetMapping("/main")
    public String main(){
        return "main";
    }
}
