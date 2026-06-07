package kr.magicbox.notification.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "fcm_token", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "token"})
})
public class FcmTokenEntity extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "token", nullable = false, length = 512)
    private String token;

    @Builder
    public FcmTokenEntity(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}
