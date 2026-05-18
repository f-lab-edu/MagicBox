package kr.magicbox.auth.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "auth_outbox")
public class AuthOutboxEntity extends BaseEntity {

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false, columnDefinition = "JSON")
    private String payload;

    @Builder
    public AuthOutboxEntity(String eventType, String payload) {
        this.eventType = eventType;
        this.payload = payload;
    }
}
