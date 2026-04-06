package kr.magicbox.creator.adapter.out.persistence.repository;

import jakarta.persistence.LockModeType;
import kr.magicbox.creator.adapter.out.persistence.entity.CreatorEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CreatorJpaRepository extends JpaRepository<CreatorEntity, Long> {

    Optional<CreatorEntity> findByUserId(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from CreatorEntity c where c.userId = :userId")
    Optional<CreatorEntity> findByUserIdWithLock(Long userId);

    Optional<CreatorEntity> findByNickname(String nickname);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from CreatorEntity c where c.nickname = :nickname")
    Optional<CreatorEntity> findByNicknameWithLock(String nickname);

    @Query("SELECT c FROM CreatorEntity c WHERE (:cursorId IS NULL OR c.id < :cursorId) ORDER BY c.id DESC")
    List<CreatorEntity> findAllByCursor(@Param("cursorId") Long cursorId, Pageable limit);

    @Query("SELECT c FROM CreatorEntity c WHERE c.nickname LIKE %:keyword% AND (:cursorId IS NULL OR c.id < :cursorId) ORDER BY c.id DESC")
    List<CreatorEntity> searchByNickname(@Param("keyword") String keyword, @Param("cursorId") Long cursorId, Pageable limit);
}
