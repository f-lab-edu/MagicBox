package kr.magicbox.subscribe.application.dto.command;

import kr.magicbox.subscribe.domain.vo.SubscriberId;

public record HandleUserRevokedCommand(SubscriberId subscriberId) {
    public static HandleUserRevokedCommand of(SubscriberId subscriberId) {
        return new HandleUserRevokedCommand(subscriberId);
    }
}
