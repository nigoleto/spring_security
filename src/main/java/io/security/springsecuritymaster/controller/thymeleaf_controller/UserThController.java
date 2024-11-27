package io.security.springsecuritymaster.controller.thymeleaf_controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserThController {

    @GetMapping("/login")
    public String login() {
        return "users/login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "users/signup";
    }

    @GetMapping("/logout")
    public String logout() {
        return "users/logout";
    }
}
