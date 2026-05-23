package kr.magicbox.payment.domain.vo;

import kr.magicbox.payment.domain.exception.InvalidFieldException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record UserId(Long value) implements UserDetails {

    public UserId {
        if (value == null || value <= 0) {
            throw new InvalidFieldException("사용자 ID는 양수여야 합니다.");
        }
    }

    public static UserId of(Long value) {
        return new UserId(value);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return value.toString();
    }
}
