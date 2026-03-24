package kr.magicbox.user.domain.exception;

import kr.magicbox.user.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DuplicateNicknameException extends BusinessException {
    public DuplicateNicknameException(String nickname) {
        super("닉네임 " + nickname + " 은 이미 사용 중입니다.", HttpStatus.CONFLICT);
    }
}