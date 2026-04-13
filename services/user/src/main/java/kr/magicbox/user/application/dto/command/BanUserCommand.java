package kr.magicbox.user.application.dto.command;

import kr.magicbox.user.domain.vo.Nickname;
import lombok.Builder;

@Builder
public record BanUserCommand(
        Nickname nickname
) {
    public static BanUserCommand of(Nickname nickname) {
        return new BanUserCommand(nickname);
    }
}
