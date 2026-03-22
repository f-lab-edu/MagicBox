package kr.magicbox.auth.adapter.out.communication.grpc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ServiceHost {
    USER("user");

    private final String hostName;
}
