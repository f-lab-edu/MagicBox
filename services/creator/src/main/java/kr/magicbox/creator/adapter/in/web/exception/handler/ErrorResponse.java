package kr.magicbox.creator.adapter.in.web.exception.handler;

import lombok.Builder;

@Builder
public record ErrorResponse(int status, String message) {
}
