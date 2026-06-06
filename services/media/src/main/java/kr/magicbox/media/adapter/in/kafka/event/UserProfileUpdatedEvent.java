package kr.magicbox.media.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record UserProfileUpdatedEvent(
        @JsonProperty("user_id") Long userId,
        @JsonProperty("before") ProfileSnapshot before,
        @JsonProperty("after") ProfileSnapshot after,
        @JsonProperty("occurred_at") Instant occurredAt
) implements InboxEvent {

    public record ProfileSnapshot(
            @JsonProperty("nickname") String nickname,
            @JsonProperty("profile_image_url") String profileImageUrl
    ) {}
}
