package io.security.springsecuritymaster.controller.rest_controller;

import io.security.springsecuritymaster.service.PubDataApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class IndexController {

    private final PubDataApiService pubDataApiService;

    public IndexController(PubDataApiService pubDataApiService) {
        this.pubDataApiService = pubDataApiService;
    }

    @GetMapping("/api")
    public String index() throws IOException {
        pubDataApiService.saveGwangju();
        return pubDataApiService.getDataApi();
    }
}
