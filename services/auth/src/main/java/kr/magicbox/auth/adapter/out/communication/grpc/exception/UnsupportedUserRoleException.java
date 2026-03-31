package kr.magicbox.auth.adapter.out.communication.grpc.exception;

import kr.magicbox.auth.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class UnsupportedUserRoleException extends BusinessException {
    public UnsupportedUserRoleException(String role) {
        super("지원하지 않는 사용자 역할입니다: " + role, HttpStatus.BAD_REQUEST);
    }
}
