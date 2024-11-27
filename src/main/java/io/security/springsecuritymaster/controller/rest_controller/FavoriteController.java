package io.security.springsecuritymaster.controller.rest_controller;

import io.security.springsecuritymaster.domain.favorite.FavoriteResponseDto;
import io.security.springsecuritymaster.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    public ResponseEntity<List<Long>> myFavorite(
            @RequestHeader(value = "Authorization", required = false) final String authHeader
    ) {
        List<Long> clothesIdList = favoriteService.myFavorite(authHeader);
        return new ResponseEntity<>(clothesIdList, HttpStatusCode.valueOf(200));
    }

    @PostMapping("/{clothesId}")
    public ResponseEntity<Void> addFavorite(
            @PathVariable Long clothesId,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        favoriteService.addFavorite(clothesId, authHeader);
        return new ResponseEntity<>(HttpStatusCode.valueOf(201));
    }

    @DeleteMapping("/{clothesId}")
    public ResponseEntity<Void> deleteFavorite(
            @PathVariable Long clothesId,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        favoriteService.deleteFavorite(clothesId, authHeader);
        return new ResponseEntity<>(HttpStatusCode.valueOf(201));

    }

}
