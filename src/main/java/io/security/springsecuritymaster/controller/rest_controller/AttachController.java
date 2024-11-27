package io.security.springsecuritymaster.controller.rest_controller;

import io.security.springsecuritymaster.service.AttachService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attach")
@RequiredArgsConstructor
public class AttachController {

    private final AttachService attachService;

    @DeleteMapping
    public ResponseEntity<Void> deleteAttach(
            @RequestParam("fileName") String fileName,
            @RequestParam("clothesId") Long clothesId
    ) {
        attachService.deleteAttach(fileName, clothesId);
        return ResponseEntity.noContent().build();
    }
}
