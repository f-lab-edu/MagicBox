package kr.magicbox.notification.adapter.out.persistence.repository;

import kr.magicbox.notification.adapter.out.persistence.entity.NotificationEntity;
import kr.magicbox.notification.domain.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findAllByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<NotificationEntity> findByIdAndUserId(Long id, Long userId);

    @Modifying
    @Query("UPDATE NotificationEntity n SET n.status = :status WHERE n.id IN :ids AND n.userId = :userId")
    void updateStatusByIdsAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userId, @Param("status") NotificationStatus status);
}
