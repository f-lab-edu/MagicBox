package kr.magicbox.review.domain.exception;

import kr.magicbox.review.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ReviewAccessDeniedException extends BusinessException {
    public ReviewAccessDeniedException() {
        super("리뷰에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }
}
