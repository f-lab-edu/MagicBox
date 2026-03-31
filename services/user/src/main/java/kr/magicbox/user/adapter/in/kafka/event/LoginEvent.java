package kr.magicbox.user.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record LoginEvent(@JsonProperty("value") Long userId) {}