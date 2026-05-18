package kr.magicbox.auth.application.dto.command;

import kr.magicbox.auth.domain.vo.UserId;
import lombok.Builder;

@Builder
public record LogoutCommand(
        UserId userId
) {
    public static LogoutCommand of(UserId userId) {
        return new LogoutCommand(userId);
    }
}
