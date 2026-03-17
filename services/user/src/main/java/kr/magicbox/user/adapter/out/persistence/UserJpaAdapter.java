package kr.magicbox.user.adapter.out.persistence;

import kr.magicbox.user.adapter.out.persistence.entity.UserEntity;
import kr.magicbox.user.adapter.out.persistence.repository.UserJpaRepository;
import kr.magicbox.user.domain.repository.UserRepository;
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
