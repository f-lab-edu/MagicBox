package kr.magicbox.review.adapter.out.persistence.mapper;

import kr.magicbox.review.adapter.out.persistence.entity.ReviewEntity;
import kr.magicbox.review.domain.aggregate.Review;
import kr.magicbox.review.domain.vo.CreatorId;
import kr.magicbox.review.domain.vo.OrderId;
import kr.magicbox.review.domain.vo.ReviewId;
import kr.magicbox.review.domain.vo.UserId;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewEntity toEntity(Review domain) {
        return ReviewEntity.builder()
                .userId(domain.getUserId().value())
                .creatorId(domain.getCreatorId().value())
                .targetId(domain.getTargetId())
                .targetType(domain.getTargetType())
                .orderId(domain.getOrderId().value())
                .rating(domain.getRating())
                .content(domain.getContent())
                .build();
    }

    public Review toDomain(ReviewEntity entity) {
        return Review.reconstructBuilder()
                .id(ReviewId.of(entity.getId()))
                .userId(UserId.of(entity.getUserId()))
                .creatorId(CreatorId.of(entity.getCreatorId()))
                .targetId(entity.getTargetId())
                .targetType(entity.getTargetType())
                .orderId(OrderId.of(entity.getOrderId()))
                .rating(entity.getRating())
                .content(entity.getContent())
                .isDeleted(entity.getIsDeleted())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(Review domain, ReviewEntity entity) {
        entity.updateFromDomain(domain);
    }
}
