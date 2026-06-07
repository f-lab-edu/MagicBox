package kr.magicbox.settlement.adapter.out.persistence.entity;

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
@Table(name = "settlement_outbox")
public class SettlementOutboxEntity extends BaseEntity {

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(columnDefinition = "JSON", nullable = false)
    private String payload;

    @Builder
    public SettlementOutboxEntity(String eventType, String payload) {
        this.eventType = eventType;
        this.payload = payload;
    }
}
