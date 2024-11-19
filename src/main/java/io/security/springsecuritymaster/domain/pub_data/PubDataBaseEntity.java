package io.security.springsecuritymaster.domain.pub_data;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class PubDataBaseEntity {

    @Column(nullable = false)
    private String address; // 주소

    @Column(nullable = false)
    private String dong; // 동

    @Column
    private String gu; // 구

    private Double latitude; // 위도

    private Double longitude; // 경도
}
