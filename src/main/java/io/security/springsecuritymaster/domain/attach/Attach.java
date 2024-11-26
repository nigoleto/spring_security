package io.security.springsecuritymaster.domain.attach;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.security.springsecuritymaster.domain.clothes.Clothes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    private String fileName;

    private String fileType;

    private String fileUrl;

    @ManyToOne
    @JoinColumn(name = "clothes_id", nullable = false)
    @JsonBackReference
    private Clothes clothes;

}
