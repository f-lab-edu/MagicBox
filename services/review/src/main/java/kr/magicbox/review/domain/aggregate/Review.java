package kr.magicbox.review.domain.aggregate;

import kr.magicbox.review.domain.enums.ReviewTargetType;
import kr.magicbox.review.domain.exception.DeletedReviewException;
import kr.magicbox.review.domain.exception.InvalidFieldException;
import kr.magicbox.review.domain.vo.CreatorId;
import kr.magicbox.review.domain.vo.OrderId;
import kr.magicbox.review.domain.vo.ReviewId;
import kr.magicbox.review.domain.vo.UserId;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
public class Review {

    private static final int RATING_MIN = 1;
    private static final int RATING_MAX = 5;
    private static final int CONTENT_MAX_LENGTH = 1000;

    private final ReviewId id;
    private final UserId userId;
    private final CreatorId creatorId;
    private final Long targetId;
    private final ReviewTargetType targetType;
    private final OrderId orderId;
    private Integer rating;
    private String content;
    private Boolean isDeleted;
    private final Instant createdAt;
    private Instant updatedAt;

    @Builder(builderMethodName = "createBuilder", builderClassName = "CreateBuilder")
    public Review(UserId userId, CreatorId creatorId, Long targetId, ReviewTargetType targetType,
                  OrderId orderId, Integer rating, String content) {
        validateCreateFields(userId, creatorId, targetId, targetType, orderId, rating, content);
        this.id = null;
        this.userId = userId;
        this.creatorId = creatorId;
        this.targetId = targetId;
        this.targetType = targetType;
        this.orderId = orderId;
        this.rating = rating;
        this.content = content;
        this.isDeleted = false;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @Builder(builderMethodName = "reconstructBuilder", builderClassName = "ReconstructBuilder")
    public Review(ReviewId id, UserId userId, CreatorId creatorId, Long targetId,
                  ReviewTargetType targetType, OrderId orderId, Integer rating, String content,
                  Boolean isDeleted, Instant createdAt, Instant updatedAt) {
        if (id == null) throw new InvalidFieldException("리뷰 ID는 필수 값입니다.");
        if (isDeleted == null) throw new InvalidFieldException("삭제 여부는 필수 값입니다.");
        this.id = id;
        this.userId = userId;
        this.creatorId = creatorId;
        this.targetId = targetId;
        this.targetType = targetType;
        this.orderId = orderId;
        this.rating = rating;
        this.content = content;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void update(Integer rating, String content) {
        if (Boolean.TRUE.equals(this.isDeleted)) {
            throw new DeletedReviewException();
        }
        validateRating(rating);
        validateContent(content);
        this.rating = rating;
        this.content = content;
        this.updatedAt = Instant.now();
    }

    public void delete() {
        if (Boolean.TRUE.equals(this.isDeleted)) {
            throw new DeletedReviewException();
        }
        this.isDeleted = true;
        this.updatedAt = Instant.now();
    }

    private void validateCreateFields(UserId userId, CreatorId creatorId, Long targetId,
                                      ReviewTargetType targetType, OrderId orderId,
                                      Integer rating, String content) {
        if (userId == null) throw new InvalidFieldException("사용자 ID는 필수 값입니다.");
        if (creatorId == null) throw new InvalidFieldException("크리에이터 ID는 필수 값입니다.");
        if (targetId == null || targetId <= 0) throw new InvalidFieldException("리뷰 대상 ID는 양수여야 합니다.");
        if (targetType == null) throw new InvalidFieldException("리뷰 대상 타입은 필수 값입니다.");
        if (orderId == null) throw new InvalidFieldException("주문 ID는 필수 값입니다.");
        validateRating(rating);
        validateContent(content);
    }

    private void validateRating(Integer rating) {
        if (rating == null || rating < RATING_MIN || rating > RATING_MAX) {
            throw new InvalidFieldException("평점은 " + RATING_MIN + "~" + RATING_MAX + " 사이의 값이어야 합니다.");
        }
    }

    private void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new InvalidFieldException("리뷰 내용은 필수 값입니다.");
        }
        if (content.length() > CONTENT_MAX_LENGTH) {
            throw new InvalidFieldException("리뷰 내용은 " + CONTENT_MAX_LENGTH + "자 이내여야 합니다.");
        }
    }
}
