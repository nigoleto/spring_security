package io.security.springsecuritymaster.repository;

import io.security.springsecuritymaster.domain.favorite.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUser_Email(String email);

    Favorite findByUser_EmailAndClothes_Id(String email, Long id);

}
