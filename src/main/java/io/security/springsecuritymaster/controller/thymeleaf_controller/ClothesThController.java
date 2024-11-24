package io.security.springsecuritymaster.controller.thymeleaf_controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/clothes")
public class ClothesThController {

    @GetMapping
    public String getClothes() { return "clothes/list"; }

    @GetMapping("/{id}")
    public String getClothesDetail() { return "clothes/detail"; }

    @GetMapping("/{id}/edit")
    public String getClothesEdit() { return "clothes/edit"; }
}