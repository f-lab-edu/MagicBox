package kr.magicbox.review.application.service;

import kr.magicbox.review.application.dto.command.DeleteReviewCommand;
import kr.magicbox.review.application.port.in.DeleteReviewUseCase;
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
public class DeleteReviewService implements DeleteReviewUseCase {

    private final ReviewRepositoryPort reviewRepositoryPort;

    @Override
    public void deleteReview(DeleteReviewCommand command) {
        Review review = reviewRepositoryPort.findById(ReviewId.of(command.reviewId()))
                .orElseThrow(ReviewNotFoundException::new);

        if (!review.getUserId().value().equals(command.userId())) {
            throw new ReviewAccessDeniedException();
        }

        review.delete();
        reviewRepositoryPort.update(review);
    }
}
