package kr.magicbox.auth.application.dto.command;

import lombok.Builder;

@Builder
public record EmailLoginCommand(String email, String password) {
}
