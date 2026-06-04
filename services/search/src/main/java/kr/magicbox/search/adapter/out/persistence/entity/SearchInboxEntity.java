package kr.magicbox.search.adapter.out.persistence.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Getter
@Table("search_inbox")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchInboxEntity {

    @Id
    private Long id;

    @Column("event_id")
    private Long eventId;

    @Column("topic")
    private String topic;

    @Column("kafka_partition")
    private Integer partition;

    @Column("kafka_offset")
    private Long offset;

    @Column("status")
    private SearchInboxStatus status;

    @CreatedDate
    @Column("created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private Instant updatedAt;

    @Builder
    public SearchInboxEntity(Long id, Long eventId, String topic, Integer partition, Long offset, SearchInboxStatus status) {
        this.id = id;
        this.eventId = eventId;
        this.topic = topic;
        this.partition = partition;
        this.offset = offset;
        this.status = status;
    }

    public SearchInboxEntity markProcessed() {
        return SearchInboxEntity.builder()
                .id(this.id)
                .eventId(this.eventId)
                .topic(this.topic)
                .partition(this.partition)
                .offset(this.offset)
                .status(SearchInboxStatus.PROCESSED)
                .build();
    }

    public SearchInboxEntity markDeadLettered() {
        return SearchInboxEntity.builder()
                .id(this.id)
                .eventId(this.eventId)
                .topic(this.topic)
                .partition(this.partition)
                .offset(this.offset)
                .status(SearchInboxStatus.DEAD_LETTERED)
                .build();
    }
}
