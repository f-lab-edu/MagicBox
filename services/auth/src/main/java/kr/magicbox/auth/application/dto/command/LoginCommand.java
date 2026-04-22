package kr.magicbox.auth.application.dto.command;

import lombok.Builder;

@Builder
public record LoginCommand(String code) {
}
