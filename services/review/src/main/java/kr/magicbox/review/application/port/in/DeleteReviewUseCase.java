package kr.magicbox.review.application.port.in;

import kr.magicbox.review.application.dto.command.DeleteReviewCommand;

public interface DeleteReviewUseCase {
    void deleteReview(DeleteReviewCommand command);
}
