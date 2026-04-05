package kr.magicbox.auth.application.dto.command;

import kr.magicbox.auth.domain.vo.UserId;
import lombok.Builder;

@Builder
public record HandleUserWithdrawnCommand(
        UserId userId
) {
    public static HandleUserWithdrawnCommand of(UserId userId) {
        return new HandleUserWithdrawnCommand(userId);
    }
}
