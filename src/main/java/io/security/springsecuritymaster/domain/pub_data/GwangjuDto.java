package io.security.springsecuritymaster.domain.pub_data;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GwangjuDto {

    @JsonProperty("설치위치(장소)")
    private String address;

    @JsonProperty("설치지역(동명)")
    private String dong;

    @JsonProperty("자치구")
    private String gu;
}
