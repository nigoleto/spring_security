package io.security.springsecuritymaster.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@Service
public class PubDataService {

    private final RestClient restClient;
    private static final Logger logger = LoggerFactory.getLogger(PubDataService.class);

    public PubDataService() {
        this.restClient = RestClient.builder()
                .baseUrl("http://api.odcloud.kr/api")
                .build();
    }

    @Value("${pubdata.api.key}")
    private String serviceKey;

    public String getApiResponse() {

//        // GET 요청을 보내고 응답을 받아옴
//        ResponseEntity<String> response = restClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/15056449/v1/uddi:6615f5a8-f41f-4c7e-862d-8550c92c2cb5")
//                        .queryParam("page", 1)
//                        .queryParam("perPage", 10)
//                        .queryParam("serviceKey", UriUtils.encodeQueryParam(serviceKey, StandardCharsets.UTF_8))
//                        .build())
//                .retrieve()
//                .toEntity(String.class);
//

        String encodedServiceKey = UriUtils.encode(serviceKey, StandardCharsets.UTF_8);

        String finalUrl = UriComponentsBuilder.fromHttpUrl("https://api.odcloud.kr/api")
                .path("/15056449/v1/uddi:6615f5a8-f41f-4c7e-862d-8550c92c2cb5")
                .queryParam("page", 1)
                .queryParam("perPage", 10)
                .queryParam("serviceKey", encodedServiceKey)
                .build()
                .toUriString();

        System.out.println("Final URL: " + finalUrl);

        ResponseEntity<String> response = restClient.get()
                .uri(finalUrl)
                .retrieve()
                .toEntity(String.class);

        logger.info("Requesting data with serviceKey: {}", serviceKey);
        logger.info("Final URL: {}", response.getHeaders().getLocation());

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("API 요청 실패: " + response.getStatusCode());
        }
    }
}
