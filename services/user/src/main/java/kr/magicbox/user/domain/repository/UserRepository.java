package kr.magicbox.user.domain.repository;

import kr.magicbox.user.adapter.out.persistence.entity.UserEntity;

import java.util.Optional;

public interface UserRepository {
    Optional<UserEntity> getUserByNickname(String nickname);
    void updateUser(UserEntity user);
}