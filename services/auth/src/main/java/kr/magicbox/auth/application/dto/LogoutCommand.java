package kr.magicbox.auth.application.dto;

import lombok.Builder;

@Builder
public record LogoutCommand(Long userId) {
}