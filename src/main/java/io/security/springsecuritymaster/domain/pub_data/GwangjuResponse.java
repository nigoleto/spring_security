package io.security.springsecuritymaster.domain.pub_data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GwangjuResponse {

    @JsonProperty("data")
    private List<GwangjuDto> data; // GwangjuDto 리스트
}
