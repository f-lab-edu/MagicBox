package kr.magicbox.user.domain.exception;

import kr.magicbox.user.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UserNotBannedException extends BaseException {
    public UserNotBannedException() {
        super("정지 상태가 아닌 사용자는 정지 해제할 수 없습니다.", HttpStatus.BAD_REQUEST);
    }
}
