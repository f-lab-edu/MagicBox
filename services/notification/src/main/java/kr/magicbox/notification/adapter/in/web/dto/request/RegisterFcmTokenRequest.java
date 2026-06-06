package kr.magicbox.notification.adapter.in.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterFcmTokenRequest(
        @JsonProperty("token") String token
) {
}
