package kr.magicbox.user.application.dto.command;

import kr.magicbox.user.domain.vo.Nickname;
import lombok.Builder;

@Builder
public record UnbanUserCommand(
        Nickname nickname
) {
    public static UnbanUserCommand of(Nickname nickname) {
        return new UnbanUserCommand(nickname);
    }
}
