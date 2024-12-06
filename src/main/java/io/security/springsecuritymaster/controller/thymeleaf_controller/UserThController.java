package io.security.springsecuritymaster.controller.thymeleaf_controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

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

//    @GetMapping("/loginSuccess")
//    public String loginSuccess(Principal principal, Model model) {
//        System.out.println("Principal class: " + principal.getClass().getName());
//        System.out.println("Principal: " + principal);
//
//        if (principal instanceof OAuth2AuthenticationToken authToken) {
//            OAuth2User oAuth2User = authToken.getPrincipal();
//            System.out.println("OAuth2User: " + oAuth2User.getAttributes());
//            model.addAttribute("user", oAuth2User.getAttributes());
//        } else {
//            throw new RuntimeException("Authentication object is not OAuth2AuthenticationToken.");
//        }
//        return "loginSuccess";
//    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(Model model) {
        return "loginSuccess";
    }

    @GetMapping("/loginFailure")
    public String loginFailure() {
        return "loginFailure";
    }
}
