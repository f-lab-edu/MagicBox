package kr.magicbox.review.domain.exception;

import kr.magicbox.review.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DeletedReviewException extends BusinessException {
    public DeletedReviewException() {
        super("삭제된 리뷰는 수정하거나 삭제할 수 없습니다.", HttpStatus.CONFLICT);
    }
}
