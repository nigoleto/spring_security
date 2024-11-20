package io.security.springsecuritymaster.controller.rest_controller;

import io.security.springsecuritymaster.domain.pub_data.Gwangju;
import io.security.springsecuritymaster.service.GwangjuService;
import io.security.springsecuritymaster.service.PubDataApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/gwangju")
@RequiredArgsConstructor
public class IndexController {

    private final PubDataApiService pubDataApiService;
    private final GwangjuService gwangjuService;

    /** 의류수거함 위치 생성 (추후 스케줄러로 특정시간에 받아오는걸로 변경 예정 ) **/
    @GetMapping("/a")
    public ResponseEntity<Void> fetchAndSaveGwangju() throws IOException {
        pubDataApiService.fetchAndSaveData();
        return new ResponseEntity<>(HttpStatusCode.valueOf(201));
    }

    /** 주변 의류수거함 조회 **/
    @GetMapping
    public ResponseEntity<List<Gwangju>> getGwangjuInRange(
            @RequestParam(required = false, defaultValue = "35.1598") double lat,
            @RequestParam(required = false, defaultValue = "126.9201") double lng) {

        return new ResponseEntity<>(gwangjuService.getGwangjuInRange(lat, lng), HttpStatusCode.valueOf(200));
    }
}
