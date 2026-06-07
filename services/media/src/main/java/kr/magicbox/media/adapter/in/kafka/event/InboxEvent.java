package kr.magicbox.media.adapter.in.kafka.event;

import java.time.Instant;

public interface InboxEvent {
    Instant occurredAt();
}
