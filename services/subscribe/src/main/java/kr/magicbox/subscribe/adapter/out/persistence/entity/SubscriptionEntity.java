package kr.magicbox.subscribe.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subscription",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_subscription_subscriber_creator",
                columnNames = {"subscriber_id", "creator_id"}
        )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SubscriptionEntity extends BaseEntity {
    @Column(name = "subscriber_id", nullable = false)
    private Long subscriberId;

    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    @Builder
    public SubscriptionEntity(Long subscriberId, Long creatorId) {
        this.subscriberId = subscriberId;
        this.creatorId = creatorId;
    }
}
