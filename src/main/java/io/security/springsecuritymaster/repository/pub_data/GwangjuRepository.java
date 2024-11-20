package io.security.springsecuritymaster.repository.pub_data;

import io.security.springsecuritymaster.domain.pub_data.Gwangju;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GwangjuRepository extends JpaRepository<Gwangju, Long> {
    Optional<Gwangju> findByAddress(String address);

    @Query("SELECT g.address FROM Gwangju g")
    List<String> findAllAddresses();

    @Query("SELECT g FROM Gwangju g WHERE g.latitude BETWEEN :latMin AND :latMax AND g.longitude BETWEEN :lngMin AND :lngMax")
    List<Gwangju> findBinsInRange(double latMin, double latMax, double lngMin, double lngMax);
}
