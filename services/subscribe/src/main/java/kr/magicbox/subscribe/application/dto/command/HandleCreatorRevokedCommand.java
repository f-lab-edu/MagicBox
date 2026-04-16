package kr.magicbox.subscribe.application.dto.command;

import kr.magicbox.subscribe.domain.vo.CreatorId;

public record HandleCreatorRevokedCommand(CreatorId creatorId) {
    public static HandleCreatorRevokedCommand of(CreatorId creatorId) {
        return new HandleCreatorRevokedCommand(creatorId);
    }
}
