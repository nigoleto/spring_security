package io.security.springsecuritymaster.controller.rest_controller;

import io.security.springsecuritymaster.service.KakaoMapApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class IndexController {

    private final KakaoMapApiService kakaoMapApiService;

    public IndexController(KakaoMapApiService kakaoMapApiService) {
        this.kakaoMapApiService = kakaoMapApiService;
    }

    @GetMapping("/api")
    public String index() throws IOException {
        return kakaoMapApiService.getDataApi();
    }
}
