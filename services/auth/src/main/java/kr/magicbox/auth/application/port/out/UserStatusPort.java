package kr.magicbox.auth.application.port.out;

public interface UserStatusPort {
    boolean isActive(Long userId);
}
