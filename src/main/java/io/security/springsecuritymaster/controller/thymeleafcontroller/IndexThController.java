package io.security.springsecuritymaster.controller.thymeleafcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

@Controller
public class IndexThController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("name", "user name");
        model.addAttribute("items", Arrays.asList("Item 1", "Item 2", "Item 3"));
        return "index";
    }
}
