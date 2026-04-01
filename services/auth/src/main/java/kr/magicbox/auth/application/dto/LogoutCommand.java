package kr.magicbox.auth.application.dto;

import kr.magicbox.auth.domain.vo.UserId;
import lombok.Builder;

@Builder
public record LogoutCommand(UserId userId) {
}