package kr.magicbox.user.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_domain_event")
public class UserDomainEventEntity extends BaseEntity {

    @Column(nullable = false)
    private String eventType;

    @Column(name = "`key`", nullable = false)
    private String key;

    @Column(nullable = false, columnDefinition = "JSON")
    private String payload;

    @Builder
    public UserDomainEventEntity(String eventType, String key, String payload) {
        this.eventType = eventType;
        this.key = key;
        this.payload = payload;
    }
}