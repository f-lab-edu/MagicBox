package kr.magicbox.auth.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthDomainEventType {

    USER_DUPLICATE_LOGGED_IN("user-duplicate-logged-in"),
    USER_LOGGED_OUT("user-logged-out"),
    USER_LOGGED_IN("user-logged-in");

    private final String value;
}
