package kr.magicbox.review.adapter.out.persistence.repository;

import kr.magicbox.review.adapter.out.persistence.entity.ReviewEntity;
import kr.magicbox.review.domain.enums.ReviewTargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, Long> {

    @Query("SELECT r FROM ReviewEntity r WHERE r.userId = :userId AND r.isDeleted = false")
    List<ReviewEntity> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT r FROM ReviewEntity r WHERE r.targetId = :targetId AND r.targetType = :targetType AND r.isDeleted = false")
    List<ReviewEntity> findAllByTarget(@Param("targetId") Long targetId, @Param("targetType") ReviewTargetType targetType);

    @Query("SELECT COALESCE(AVG(r.rating), 0.0) FROM ReviewEntity r WHERE r.creatorId = :creatorId AND r.isDeleted = false")
    double calculateAverageRatingByCreatorId(@Param("creatorId") Long creatorId);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM ReviewEntity r WHERE r.orderId = :orderId")
    boolean existsByOrderId(@Param("orderId") Long orderId);
}
