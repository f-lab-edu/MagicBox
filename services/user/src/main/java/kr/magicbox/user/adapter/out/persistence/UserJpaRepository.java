package kr.magicbox.user.adapter.out.persistence;

import kr.magicbox.user.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    @Query("select u from UserEntity u where u.nickname = :nickname")
    Optional<UserEntity> findByNickname(String nickname);
}
