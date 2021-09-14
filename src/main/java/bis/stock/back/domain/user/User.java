package bis.stock.back.domain.user;

import bis.stock.back.domain.holdingStock.HoldingStock;
import bis.stock.back.domain.user.dto.UserUpdateDto;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Table(name = "user")
@Entity
public class User implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private String nickname;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user")
    private List<HoldingStock> holdingStocks;

    // 생성 시간 저장
    @PrePersist
    public void createdAtGenerator() {
        this.createdAt = LocalDateTime.now();
    }

    @Column
    private Long cash;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private final List<UserRole> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(UserRole::toString)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean  isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void update(UserUpdateDto userUpdateDto) {
        this.email = userUpdateDto.getEmail();
        this.password = userUpdateDto.getPassword();
        this.nickname = userUpdateDto.getNickname();
    }
}
