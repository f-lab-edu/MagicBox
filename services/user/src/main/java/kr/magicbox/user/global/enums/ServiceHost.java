package kr.magicbox.user.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ServiceHost {
    REVIEW("review");

    private final String hostName;
}
