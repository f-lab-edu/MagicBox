package kr.magicbox.review.application.port.in;

import kr.magicbox.review.application.dto.command.UpdateReviewCommand;

public interface UpdateReviewUseCase {
    void updateReview(UpdateReviewCommand command);
}
