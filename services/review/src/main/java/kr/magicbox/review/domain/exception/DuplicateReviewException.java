package kr.magicbox.review.domain.exception;

import kr.magicbox.review.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DuplicateReviewException extends BusinessException {
    public DuplicateReviewException() {
        super("해당 주문에 대한 리뷰가 이미 존재합니다.", HttpStatus.CONFLICT);
    }
}
