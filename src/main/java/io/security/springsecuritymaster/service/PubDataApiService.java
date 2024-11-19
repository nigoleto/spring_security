package io.security.springsecuritymaster.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.security.springsecuritymaster.domain.pub_data.Gwangju;
import io.security.springsecuritymaster.domain.pub_data.GwangjuDto;
import io.security.springsecuritymaster.domain.pub_data.GwangjuResponse;
import io.security.springsecuritymaster.repository.pub_data.GwangjuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class PubDataApiService {

    @Value("${pubdata.api.key}")
    private String serviceKey;
    private final GwangjuRepository gwangjuRepository;

    // DB에 데이터 저장
    public void saveGwangju() throws IOException {
        String jsonData = getDataApi(); // API 호출하여 JSON 데이터를 가져옴

        // JSON 데이터를 Java 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        GwangjuResponse response = objectMapper.readValue(jsonData, GwangjuResponse.class);

        List<GwangjuDto> gwangjuDtoList = response.getData();

        // DB에서 이미 저장된 address 목록 조회
        Set<String> existingAddresses = new HashSet<>(gwangjuRepository.findAllAddresses());

        // DTO를 Entity로 변환하고 중복 제거
        List<Gwangju> gwangjuList = gwangjuDtoList.stream()
                .map(dto -> Gwangju.builder()
                        .address(dto.getAddress())
                        .dong(dto.getDong())
                        .gu(dto.getGu())
                        .build())
                .filter(entity -> !existingAddresses.contains(entity.getAddress())) // 중복 제거
                .toList();

        // 중복 제거된 데이터 저장
        gwangjuRepository.saveAll(gwangjuList);
    }


    // 공공데이터포털 api로 데이터 받아오기
    public String getDataApi() throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://api.odcloud.kr/api/15056449/v1/uddi:6615f5a8-f41f-4c7e-862d-8550c92c2cb5?"); /*URL*/
        // 오픈 API의요청 규격에 맞는 파라미터 생성, 발급받은 인증키.
        String encodedServiceKey = UriUtils.encode(serviceKey, StandardCharsets.UTF_8); // 키 인코딩
        urlBuilder.append("?" + URLEncoder.encode("page","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("perPage","UTF-8") + "=" + URLEncoder.encode("5", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("serviceKey","UTF-8") + "=" + encodedServiceKey); /*Service Key*/
        // URL 객체 생성.
        URL url = new URL(urlBuilder.toString());
        // 요청하고자 하는 URL과 통신하기 위한 Connection 객체 생성.
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 통신을 위한 메소드 SET.
        conn.setRequestMethod("GET");
        // 통신을 위한 Content-type SET.
        conn.setRequestProperty("Content-type", "application/json");
        // 통신 응답 코드 확인.
        System.out.println("Response code: " + conn.getResponseCode());
        // 전달받은 데이터를 BufferedReader 객체로 저장.
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        // 저장된 데이터를 라인별로 읽어 StringBuilder 객체로 저장.
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        // 객체 해제.
        rd.close();
        conn.disconnect();
        // 전달받은 데이터 확인.

        return sb.toString();
    }
}
