package com.example.myDiningReview.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThymeleafController {
    @GetMapping("/login")
    public String getLoginView() {
        return "login";
    }

    @GetMapping("/index")
    public String getIndexView() {
        return "index";
    }
}
