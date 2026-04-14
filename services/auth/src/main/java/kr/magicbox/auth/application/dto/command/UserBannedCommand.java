package kr.magicbox.auth.application.dto.command;

import kr.magicbox.auth.domain.vo.UserId;
import lombok.Builder;

@Builder
public record UserBannedCommand(
        UserId userId
) {
    public static UserBannedCommand of(UserId userId) {
        return new UserBannedCommand(userId);
    }
}