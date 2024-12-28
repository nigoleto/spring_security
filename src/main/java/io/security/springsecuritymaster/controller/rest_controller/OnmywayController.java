package io.security.springsecuritymaster.controller.rest_controller;

import io.security.springsecuritymaster.service.OnmywayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/onmyway")
@RequiredArgsConstructor
public class OnmywayController {

    private final OnmywayService onmywayService;

    @GetMapping("/{clothesId}")
    public ResponseEntity<Long> countOnmyway(
            @PathVariable Long clothesId
    ) {
        long count = onmywayService.countOnmyway(clothesId);
        return new ResponseEntity<>(count, HttpStatusCode.valueOf(200));
    }

    @PostMapping("/{clothesId}")
    public ResponseEntity<Void> addOnmyway(
            @PathVariable Long clothesId,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        onmywayService.addOnmyway(clothesId, authHeader);
        return new ResponseEntity<>(HttpStatusCode.valueOf(201));
    }

    @DeleteMapping("/{clothesId}")
    public ResponseEntity<Void> deleteOnmyway(
            @PathVariable Long clothesId,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        onmywayService.deleteOnmyway(clothesId, authHeader);
        return new ResponseEntity<>(HttpStatusCode.valueOf(201));
    }
}
