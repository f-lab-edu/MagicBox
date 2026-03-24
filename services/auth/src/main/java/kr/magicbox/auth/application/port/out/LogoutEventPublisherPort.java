package kr.magicbox.auth.application.port.out;

import kr.magicbox.auth.domain.event.UserLoggedOutEvent;

public interface LogoutEventPublisherPort {
    void publish(UserLoggedOutEvent event);
}