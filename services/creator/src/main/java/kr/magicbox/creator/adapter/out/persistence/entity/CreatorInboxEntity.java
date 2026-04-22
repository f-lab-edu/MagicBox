package kr.magicbox.creator.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "creator_inbox")
public class CreatorInboxEntity extends BaseEntity {

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
    private CreatorInboxStatus status;

    @Builder
    public CreatorInboxEntity(Long eventId, String topic, Integer partition, Long offset, CreatorInboxStatus status) {
        this.eventId = eventId;
        this.topic = topic;
        this.partition = partition;
        this.offset = offset;
        this.status = status;
    }

    public void markProcessed() {
        this.status = CreatorInboxStatus.PROCESSED;
    }

    public void markDeadLettered() {
        this.status = CreatorInboxStatus.DEAD_LETTERED;
    }
}