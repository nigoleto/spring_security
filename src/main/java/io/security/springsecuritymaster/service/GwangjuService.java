package io.security.springsecuritymaster.service;

import io.security.springsecuritymaster.domain.pub_data.Gwangju;
import io.security.springsecuritymaster.repository.pub_data.GwangjuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GwangjuService {

    private final GwangjuRepository gwangjuRepository;

    public List<Gwangju> getGwangjuInRange(double lat, double lng) {

        double latMin = lat - 0.01;
        double latMax = lat + 0.01;
        double lngMin = lng - 0.01;
        double lngMax = lng + 0.01;

        return gwangjuRepository.findBinsInRange(latMin, latMax, lngMin, lngMax);
    }
}
