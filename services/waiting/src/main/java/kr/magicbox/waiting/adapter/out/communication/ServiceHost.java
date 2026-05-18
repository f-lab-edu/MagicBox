package kr.magicbox.waiting.adapter.out.communication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ServiceHost {
    RELEASE("release-service");

    private final String hostName;
}
