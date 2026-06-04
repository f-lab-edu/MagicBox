package kr.magicbox.search.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "search_inbox")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchInboxEntity extends BaseEntity {

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
    private SearchInboxStatus status;

    @Builder
    public SearchInboxEntity(Long eventId, String topic, Integer partition, Long offset, SearchInboxStatus status) {
        this.eventId = eventId;
        this.topic = topic;
        this.partition = partition;
        this.offset = offset;
        this.status = status;
    }

    public void markProcessed() {
        this.status = SearchInboxStatus.PROCESSED;
    }

    public void markDeadLettered() {
        this.status = SearchInboxStatus.DEAD_LETTERED;
    }
}
