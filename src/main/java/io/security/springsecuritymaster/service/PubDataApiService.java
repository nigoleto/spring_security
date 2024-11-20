package io.security.springsecuritymaster.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.security.springsecuritymaster.domain.pub_data.Gwangju;
import io.security.springsecuritymaster.domain.pub_data.GwangjuResponse;
import io.security.springsecuritymaster.repository.pub_data.GwangjuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class PubDataApiService {

    @Value("${pubdata.api.key}")
    private String serviceKey;
    private final RestClient restClient;
    private final GwangjuRepository gwangjuRepository;
    private final GeocodingService geocodingService;

    /** 공공데이터포털 api **/
    public void fetchAndSaveData() throws JsonProcessingException {
        // 1. API 호출 URL 생성
        String url = "http://api.odcloud.kr/api/15056449/v1/uddi:6615f5a8-f41f-4c7e-862d-8550c92c2cb5"
                + "?page=" + "2"
                + "&perPage=" + "25"
                + "&serviceKey=" + UriUtils.encode(serviceKey, StandardCharsets.UTF_8);

        URI uri = URI.create(url); // RestClient나 RestTemplate에서는 인코딩 할 때 + 나 / 같은 특수기호를 인코딩 해주지 않기 때문에 명시적으로 uri 형성

        // 2. 데이터 가져오기
        ResponseEntity<String> response = restClient.get()
                .uri(uri)
                .retrieve()
                .toEntity(String.class);

        // 3. JSON 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        GwangjuResponse gwangjuResponse = objectMapper.readValue(response.getBody(), GwangjuResponse.class);

        // 4. DB에서 이미 저장된 address 목록 조회
        Set<String> existingAddresses = new HashSet<>(gwangjuRepository.findAllAddresses());

        // 5. DTO -> Entity
        List<Gwangju> clothingBins = gwangjuResponse.getData().stream()
                .map(dto -> Gwangju.builder()
                        .address(dto.getAddress())
                        .dong(dto.getDong())
                        .gu(dto.getGu())
                        .latitude(geocodingService.getCoordinates(dto).getLatitude())
                        .longitude(geocodingService.getCoordinates(dto).getLongitude())
                        .build())
                .filter(bin -> !existingAddresses.contains(bin.getAddress())) // 중복 제거
                .toList();

        // 6. repository 저장
        gwangjuRepository.saveAll(clothingBins);
    }

}
