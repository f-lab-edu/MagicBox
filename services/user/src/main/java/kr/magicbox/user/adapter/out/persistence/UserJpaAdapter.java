package kr.magicbox.user.adapter.out.persistence;

import kr.magicbox.user.adapter.out.persistence.mapper.UserMapper;
import kr.magicbox.user.adapter.out.persistence.repository.UserJpaRepository;
import kr.magicbox.user.domain.aggregate.User;
import kr.magicbox.user.application.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserJpaAdapter implements UserRepositoryPort {
    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;


    @Override
    public Optional<User> getUserByNickname(String nickname) {
        return userJpaRepository.findByNickname(nickname)
                .map(userMapper::toDomain);
    }

    @Override
    public void updateUser(User user) {
        userJpaRepository.findById(user.getId())
                .ifPresent(entity -> {
                    userMapper.updateEntity(user, entity);
                    userJpaRepository.save(entity);
                });
    }
}
