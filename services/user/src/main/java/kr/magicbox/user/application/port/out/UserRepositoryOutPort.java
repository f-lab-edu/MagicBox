package kr.magicbox.user.application.port.out;

import kr.magicbox.user.domain.aggregate.User;

import java.util.Optional;

public interface UserRepositoryOutPort {
    Optional<User> getUserByNickname(String nickname);
    void updateUser(User user);
}