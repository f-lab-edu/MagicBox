package kr.magicbox.creator.application.dto.command;

import kr.magicbox.creator.domain.vo.Nickname;
import lombok.Builder;

@Builder
public record UnbanCreatorCommand(
        Nickname nickname
) {
    public static UnbanCreatorCommand of(Nickname nickname) {
        return new UnbanCreatorCommand(nickname);
    }
}
