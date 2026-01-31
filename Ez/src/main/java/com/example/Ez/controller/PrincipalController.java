package com.example.Ez.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PrincipalController {

    @GetMapping
    public String index() {
        return "principal/index";
    }

    @GetMapping("/login")
    public String login() {
        return "principal/login";
    }

    @GetMapping("/signin")
    public String signin() {
        return "principal/sign_in";
    }
}
