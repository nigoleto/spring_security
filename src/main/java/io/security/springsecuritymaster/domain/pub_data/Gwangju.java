package io.security.springsecuritymaster.domain.pub_data;

import jakarta.persistence.*;
import lombok.*;

@Table
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Gwangju {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String address; // 주소

    @Column(nullable = false)
    private String dong; // 동

    @Column
    private String gu; // 구

    private Double latitude; // 위도

    private Double longitude; // 경도

}
