package kr.magicbox.review.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import kr.magicbox.review.domain.aggregate.Review;
import kr.magicbox.review.domain.enums.ReviewTargetType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "review",
        uniqueConstraints = @UniqueConstraint(name = "uk_review_order_id", columnNames = "order_id"),
        indexes = {
                @Index(name = "idx_review_creator_id", columnList = "creator_id"),
                @Index(name = "idx_review_user_id", columnList = "user_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewEntity extends BaseEntity {

    @Version
    private Long version;

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "creator_id", nullable = false, updatable = false)
    private Long creatorId;

    @Column(name = "target_id", nullable = false, updatable = false)
    private Long targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, updatable = false)
    private ReviewTargetType targetType;

    @Column(name = "order_id", nullable = false, updatable = false)
    private Long orderId;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Builder
    public ReviewEntity(Long userId, Long creatorId, Long targetId, ReviewTargetType targetType,
                        Long orderId, Integer rating, String content) {
        this.userId = userId;
        this.creatorId = creatorId;
        this.targetId = targetId;
        this.targetType = targetType;
        this.orderId = orderId;
        this.rating = rating;
        this.content = content;
        this.isDeleted = false;
    }

    public void updateFromDomain(Review domain) {
        this.rating = domain.getRating();
        this.content = domain.getContent();
        this.isDeleted = domain.getIsDeleted();
    }
}
