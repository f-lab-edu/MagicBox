package kr.magicbox.auth.application.dto;

import lombok.Builder;

@Builder
public record ExchangeTokenCommand(String code) {
}