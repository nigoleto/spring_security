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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clothes")
public class ClothesController {

    private final ClothesService clothesService;

    @PostMapping
    public ResponseEntity<Void> addClothes(@Valid @RequestBody ClothesRequestDto clothesRequestDto) {
        clothesService.addClothes(clothesRequestDto);
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
    public ResponseEntity<Void> updateClothes(@PathVariable Long id, @RequestBody ClothesRequestDto clothesRequestDto) {

        clothesService.updateClothes(id, clothesRequestDto);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClothes(@PathVariable Long id) {
        clothesService.deleteClothes(id);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

}
