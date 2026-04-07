package kr.magicbox.creator.application.dto.command;

import kr.magicbox.creator.domain.vo.Nickname;
import lombok.Builder;

@Builder
public record BanCreatorCommand(
        Nickname nickname
) {
    public static BanCreatorCommand of(Nickname nickname) {
        return new BanCreatorCommand(nickname);
    }
}
