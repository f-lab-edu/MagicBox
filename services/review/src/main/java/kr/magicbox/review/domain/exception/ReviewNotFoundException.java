package kr.magicbox.review.domain.exception;

import kr.magicbox.review.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ReviewNotFoundException extends BusinessException {
    public ReviewNotFoundException() {
        super("리뷰를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
