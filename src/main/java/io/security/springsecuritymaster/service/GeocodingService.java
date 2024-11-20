package io.security.springsecuritymaster.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.security.springsecuritymaster.domain.pub_data.Gwangju;
import io.security.springsecuritymaster.domain.pub_data.GwangjuDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class GeocodingService {

    @Value("${kakaomap.api.key}")
    private String kakaomapKey;

    private final RestClient restClient;

    public Gwangju getCoordinates(GwangjuDto gwangjuDto) {
        String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + UriUtils.encode("광주 " + gwangjuDto.getAddress(), StandardCharsets.UTF_8);

        URI uri = URI.create(url);

        String response = restClient.get()
                .uri(uri)
                .header("Authorization", "KakaoAK " + kakaomapKey)
                .retrieve()
                .body(String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode responseJson = null;
        try {
            responseJson = mapper.readTree(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        JsonNode firstAddress = responseJson.get("documents").get(0);

        return Gwangju.builder()
                .latitude(firstAddress.get("y").asDouble())
                .longitude(firstAddress.get("x").asDouble())
                .build();
    }
}