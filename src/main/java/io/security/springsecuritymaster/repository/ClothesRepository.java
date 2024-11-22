package io.security.springsecuritymaster.repository;

import io.security.springsecuritymaster.domain.clothes.Clothes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClothesRepository extends JpaRepository<Clothes,Long> {

    Page<Clothes> findByDeletedAtNullOrderByCreatedAtDesc(Pageable pageable);

    Page<Clothes> findByTitleContainsAndDeletedAtNullOrderByCreatedAtDesc(String title, Pageable pageable);

}
