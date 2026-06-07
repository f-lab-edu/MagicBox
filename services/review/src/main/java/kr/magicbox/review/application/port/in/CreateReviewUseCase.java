package kr.magicbox.review.application.port.in;

import kr.magicbox.review.application.dto.command.CreateReviewCommand;

public interface CreateReviewUseCase {
    void createReview(CreateReviewCommand command);
}
