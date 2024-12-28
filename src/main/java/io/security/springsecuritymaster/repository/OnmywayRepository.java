package io.security.springsecuritymaster.repository;

import io.security.springsecuritymaster.domain.onmyway.Onmyway;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnmywayRepository extends JpaRepository<Onmyway, Long> {
    Onmyway findByUser_EmailAndClothes_Id(String email, Long id);

    long countByClothes_Id(Long id);

    boolean existsByUser_EmailAndClothes_Id(String email, Long id);
}
