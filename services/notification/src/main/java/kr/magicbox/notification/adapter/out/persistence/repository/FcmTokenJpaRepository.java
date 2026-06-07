package kr.magicbox.notification.adapter.out.persistence.repository;

import kr.magicbox.notification.adapter.out.persistence.entity.FcmTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmTokenJpaRepository extends JpaRepository<FcmTokenEntity, Long> {
    boolean existsByUserIdAndToken(Long userId, String token);
}
