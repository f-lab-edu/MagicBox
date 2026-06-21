package kr.magicbox.auth.application.port.out;

import java.util.concurrent.CompletableFuture;

public interface UserStatusPort {
    CompletableFuture<Boolean> isActive(Long userId);
}
