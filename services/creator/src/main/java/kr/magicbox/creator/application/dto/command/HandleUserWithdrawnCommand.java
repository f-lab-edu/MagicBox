package kr.magicbox.creator.application.dto.command;

import kr.magicbox.creator.domain.vo.UserId;
import lombok.Builder;

@Builder
public record HandleUserWithdrawnCommand(
        UserId userId
) {
    public static HandleUserWithdrawnCommand of(UserId userId) {
        return new HandleUserWithdrawnCommand(userId);
    }
}
