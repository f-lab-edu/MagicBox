package kr.magicbox.auth.application.dto.command;

import kr.magicbox.auth.domain.vo.UserId;
import lombok.Builder;

@Builder
public record HandleUserBannedCommand(
        UserId userId
) {
    public static HandleUserBannedCommand of(UserId userId) {
        return new HandleUserBannedCommand(userId);
    }
}
