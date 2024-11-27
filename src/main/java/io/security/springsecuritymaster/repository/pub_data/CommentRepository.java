package io.security.springsecuritymaster.repository.pub_data;

import io.security.springsecuritymaster.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByClothesIdAndDeletedAtNullOrderByCreatedAtDesc(Long id);
}
