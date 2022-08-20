package com.example.myCinema;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class GeneralTemplates {
    
    @GetMapping
    public String getIndexPage() {
        return "index";
    }


    @GetMapping("/start") 
    public String getStartPage() {
        return "start";
    }
}