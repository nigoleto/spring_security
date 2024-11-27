package io.security.springsecuritymaster.repository;

import io.security.springsecuritymaster.domain.attach.Attach;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachRepository extends JpaRepository<Attach, Long> {
    Attach findByClothesIdAndFileName(Long id, String fileName);
}
