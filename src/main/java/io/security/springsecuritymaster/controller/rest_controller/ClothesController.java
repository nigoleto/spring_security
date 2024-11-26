package io.security.springsecuritymaster.controller.rest_controller;

import io.security.springsecuritymaster.domain.clothes.ClothesRequestDto;
import io.security.springsecuritymaster.domain.clothes.ClothesResponseDto;
import io.security.springsecuritymaster.service.ClothesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clothes")
public class ClothesController {

    private final ClothesService clothesService;

    @PostMapping
    public ResponseEntity<Void> addClothes(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @Valid @RequestPart(value = "clothesRequestDto") ClothesRequestDto clothesRequestDto,
            @RequestPart(value = "file", required = false) MultipartFile attach
    ) throws IOException {
        clothesService.addClothes(authHeader, clothesRequestDto, attach);
        return new ResponseEntity<>(HttpStatusCode.valueOf(201));
    }

    @GetMapping
    public ResponseEntity<Page<ClothesResponseDto>> getAllClothes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = false) String keyword
    ) {

        Page<ClothesResponseDto> clothesList = clothesService.getAllClothes(page, size, keyword);

        return new ResponseEntity<>(clothesList, HttpStatusCode.valueOf(200));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClothesResponseDto> getClothes(@PathVariable Long id) {
        return new ResponseEntity<>(clothesService.getClothes(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateClothes(
            @PathVariable Long id,
            @Valid @RequestPart(value = "clothesRequestDto") ClothesRequestDto clothesRequestDto,
            @RequestPart(value = "file", required = false) MultipartFile attach

    ) throws IOException {

        clothesService.updateClothes(id, clothesRequestDto, attach);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClothes(@PathVariable Long id) {
        clothesService.deleteClothes(id);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

}
