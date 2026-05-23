package kr.magicbox.review.application.service;

import kr.magicbox.review.application.dto.command.CreateReviewCommand;
import kr.magicbox.review.application.port.in.CreateReviewUseCase;
import kr.magicbox.review.application.port.out.ReviewRepositoryPort;
import kr.magicbox.review.domain.aggregate.Review;
import kr.magicbox.review.domain.vo.CreatorId;
import kr.magicbox.review.domain.vo.OrderId;
import kr.magicbox.review.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateReviewService implements CreateReviewUseCase {

    private final ReviewRepositoryPort reviewRepositoryPort;

    @Override
    public void createReview(CreateReviewCommand command) {
        Review review = Review.createBuilder()
                .userId(UserId.of(command.userId()))
                .creatorId(CreatorId.of(command.creatorId()))
                .targetId(command.targetId())
                .targetType(command.targetType())
                .orderId(OrderId.of(command.orderId()))
                .rating(command.rating())
                .content(command.content())
                .build();

        reviewRepositoryPort.save(review);
    }
}
