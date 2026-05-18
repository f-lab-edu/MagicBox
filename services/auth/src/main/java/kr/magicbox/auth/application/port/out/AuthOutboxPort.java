package kr.magicbox.auth.application.port.out;

import kr.magicbox.auth.domain.event.AuthDomainEvent;

public interface AuthOutboxPort {
    void save(AuthDomainEvent event);
}
