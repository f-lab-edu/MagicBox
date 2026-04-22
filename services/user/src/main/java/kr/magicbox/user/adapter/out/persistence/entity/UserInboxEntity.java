package kr.magicbox.user.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_inbox")
public class UserInboxEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private Long eventId;

    @Column(nullable = false)
    private String topic;

    @Column(name = "kafka_partition", nullable = false)
    private Integer partition;

    @Column(name = "kafka_offset", nullable = false)
    private Long offset;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserInboxStatus status;

    @Builder
    public UserInboxEntity(Long eventId, String topic, Integer partition, Long offset, UserInboxStatus status) {
        this.eventId = eventId;
        this.topic = topic;
        this.partition = partition;
        this.offset = offset;
        this.status = status;
    }

    public void markProcessed() {
        this.status = UserInboxStatus.PROCESSED;
    }

    public void markDeadLettered() {
        this.status = UserInboxStatus.DEAD_LETTERED;
    }
}