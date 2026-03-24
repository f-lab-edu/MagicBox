package kr.magicbox.user.application.dto;

import lombok.Builder;

@Builder
public record LoadUserCredentialResult(Long userId, String userRole) {
}
