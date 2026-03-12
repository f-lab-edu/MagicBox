package kr.magicbox.user.application.port.out.repository;

import kr.magicbox.user.domain.entity.UserEntity;

import java.util.Optional;

public interface UserRepository {
    Optional<UserEntity> getUserByNickname(String nickname);
    void updateUser(UserEntity user);
}
