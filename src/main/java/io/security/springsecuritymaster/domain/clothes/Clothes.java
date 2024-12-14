package io.security.springsecuritymaster.domain.clothes;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.security.springsecuritymaster.domain.BaseEntity;
import io.security.springsecuritymaster.domain.attach.Attach;
import io.security.springsecuritymaster.domain.pub_data.Gwangju;
import io.security.springsecuritymaster.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Clothes extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "gwangju_id", nullable = true)
    private Gwangju gwangju;

    @OneToMany(mappedBy = "clothes", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Attach> attachList;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 5000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column
    private Gender gender;

    @Column
    private String size;

    @Column
    private String status;

    private int viewCount = 0;

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void update(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void delete() {
        super.delete();
    }
}
