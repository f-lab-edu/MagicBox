package kr.magicbox.review.adapter.out.persistence;

import kr.magicbox.review.adapter.out.persistence.entity.ReviewEntity;
import kr.magicbox.review.adapter.out.persistence.mapper.ReviewMapper;
import kr.magicbox.review.adapter.out.persistence.repository.ReviewJpaRepository;
import kr.magicbox.review.application.port.out.ReviewRepositoryPort;
import kr.magicbox.review.domain.aggregate.Review;
import kr.magicbox.review.domain.enums.ReviewTargetType;
import kr.magicbox.review.domain.exception.DuplicateReviewException;
import kr.magicbox.review.domain.exception.ReviewNotFoundException;
import kr.magicbox.review.domain.vo.CreatorId;
import kr.magicbox.review.domain.vo.ReviewId;
import kr.magicbox.review.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReviewJpaAdapter implements ReviewRepositoryPort {

    private final ReviewJpaRepository reviewJpaRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public void save(Review review) {
        try {
            reviewJpaRepository.save(reviewMapper.toEntity(review));
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateReviewException();
        }
    }

    @Override
    public void update(Review review) {
        ReviewEntity entity = reviewJpaRepository.findById(review.getId().value())
                .orElseThrow(ReviewNotFoundException::new);
        reviewMapper.updateEntity(review, entity);
    }

    @Override
    public Optional<Review> findById(ReviewId reviewId) {
        return reviewJpaRepository.findById(reviewId.value())
                .map(reviewMapper::toDomain);
    }

    @Override
    public List<Review> findAllByUserId(UserId userId) {
        return reviewJpaRepository.findAllByUserId(userId.value()).stream()
                .map(reviewMapper::toDomain)
                .toList();
    }

    @Override
    public List<Review> findAllByTarget(Long targetId, ReviewTargetType targetType) {
        return reviewJpaRepository.findAllByTarget(targetId, targetType).stream()
                .map(reviewMapper::toDomain)
                .toList();
    }

    @Override
    public double calculateAverageRatingByCreatorId(CreatorId creatorId) {
        return reviewJpaRepository.calculateAverageRatingByCreatorId(creatorId.value());
    }

    @Override
    public boolean existsByOrderId(Long orderId) {
        return reviewJpaRepository.existsByOrderId(orderId);
    }
}
