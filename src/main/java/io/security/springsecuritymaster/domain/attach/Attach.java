package io.security.springsecuritymaster.domain.attach;

import io.security.springsecuritymaster.domain.clothes.Clothes;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Attach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "clothes_id", nullable = false)
    private Clothes clothes;

}
