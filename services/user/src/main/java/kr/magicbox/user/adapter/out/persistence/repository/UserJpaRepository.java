package kr.magicbox.user.adapter.out.persistence.repository;

import jakarta.persistence.LockModeType;
import kr.magicbox.user.adapter.out.persistence.entity.UserEntity;
import kr.magicbox.user.domain.enums.OAuth2Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    @Query("select u from UserEntity u where u.nickname = :nickname")
    Optional<UserEntity> findByNickname(String nickname);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from UserEntity u where u.id = :id")
    Optional<UserEntity> findByIdWithLock(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from UserEntity u where u.nickname = :nickname")
    Optional<UserEntity> findByNicknameWithLock(String nickname);

    @Query("select u from UserEntity u where u.oauth2Id = :oauth2Id and u.oauth2Provider = :provider")
    Optional<UserEntity> findByOauth2IdAndOauth2Provider(String oauth2Id, OAuth2Provider provider);
}
