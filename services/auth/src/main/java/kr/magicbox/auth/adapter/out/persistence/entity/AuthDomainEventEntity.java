package kr.magicbox.auth.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "auth_domain_event")
public class AuthDomainEventEntity extends BaseEntity{

    @Column(nullable = false)
    private String eventType;

    @Column(name = "`key`", nullable = false)
    private String key;

    @Column(nullable = false, columnDefinition = "JSON")
    private String payload;

    @Builder
    public AuthDomainEventEntity(String eventType, String key, String payload) {
        this.eventType = eventType;
        this.key = key;
        this.payload = payload;
    }
}
