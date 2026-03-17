package kr.magicbox.user.domain.repository;

import kr.magicbox.user.domain.aggregate.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> getUserByNickname(String nickname);
    void updateUser(User user);
}