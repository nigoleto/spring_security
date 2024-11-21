package io.security.springsecuritymaster.domain.comment;

import io.security.springsecuritymaster.domain.BaseEntity;
import io.security.springsecuritymaster.domain.clothes.Clothes;
import io.security.springsecuritymaster.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "clothes_id", nullable = false)
    private Clothes clothes;

    @Column(nullable = false, length = 1000)
    private String content;
}
