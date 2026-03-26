package kr.magicbox.user.application.port.out;

import kr.magicbox.user.domain.aggregate.User;
import kr.magicbox.user.domain.enums.OAuth2Provider;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> getUserByNickname(String nickname);
    Optional<User> getUserById(Long userId);
    Optional<User> findByOauth2IdAndProvider(String oauth2Id, OAuth2Provider provider);
    User saveUser(User user);
    void updateUser(User user);
}