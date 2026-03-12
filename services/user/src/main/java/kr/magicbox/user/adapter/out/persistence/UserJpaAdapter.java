package kr.magicbox.user.adapter.out.persistence;

import kr.magicbox.user.application.port.out.repository.UserRepository;
import kr.magicbox.user.domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserJpaAdapter implements UserRepository {
    private final UserJpaRepository userJpaRepository;


    @Override
    public Optional<UserEntity> getUserByNickname(String nickname) {
        return userJpaRepository.findByNickname(nickname);
    }

    @Override
    public void updateUser(UserEntity user) {
        userJpaRepository.save(user);
    }
}
