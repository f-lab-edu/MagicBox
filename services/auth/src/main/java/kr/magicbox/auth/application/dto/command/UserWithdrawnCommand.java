package kr.magicbox.auth.application.dto.command;

import kr.magicbox.auth.domain.vo.UserId;
import lombok.Builder;

@Builder
public record UserWithdrawnCommand(
        UserId userId
) {
    public static UserWithdrawnCommand of(UserId userId) {
        return new UserWithdrawnCommand(userId);
    }
}