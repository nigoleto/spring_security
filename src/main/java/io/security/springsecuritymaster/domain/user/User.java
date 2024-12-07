package io.security.springsecuritymaster.domain.user;

import io.security.springsecuritymaster.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String email;

    @Column(nullable = false, length = 1000)
    private String password;

    @Column(nullable = false
            , length = 12
//            , unique = true
    )
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private boolean isEmailVerified = false;

    @Builder
    private User(String email, String password, String nickname, boolean isEmailVerified) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.isEmailVerified = isEmailVerified;
    }

    public User(Long id) {
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.getAuthority()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    public void activateUser() {
        this.isActive = true;
    }

    public void deactivateUser() {
        this.isActive = false;
    }

    public void verifiedUser() {
        this.isEmailVerified = true;
    }

}
