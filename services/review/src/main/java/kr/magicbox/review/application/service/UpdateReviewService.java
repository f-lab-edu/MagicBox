package kr.magicbox.review.application.service;

import kr.magicbox.review.application.dto.command.UpdateReviewCommand;
import kr.magicbox.review.application.port.in.UpdateReviewUseCase;
import kr.magicbox.review.application.port.out.ReviewRepositoryPort;
import kr.magicbox.review.domain.aggregate.Review;
import kr.magicbox.review.domain.exception.ReviewAccessDeniedException;
import kr.magicbox.review.domain.exception.ReviewNotFoundException;
import kr.magicbox.review.domain.vo.ReviewId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateReviewService implements UpdateReviewUseCase {

    private final ReviewRepositoryPort reviewRepositoryPort;

    @Override
    public void updateReview(UpdateReviewCommand command) {
        Review review = reviewRepositoryPort.findById(ReviewId.of(command.reviewId()))
                .orElseThrow(ReviewNotFoundException::new);

        if (!review.getUserId().value().equals(command.userId())) {
            throw new ReviewAccessDeniedException();
        }

        review.update(command.rating(), command.content());
        reviewRepositoryPort.update(review);
    }
}
