package kr.magicbox.shortform.adapter.out.communication.grpc.exception;

import kr.magicbox.shortform.global.exception.SystemError;

public class UserServiceUnavailableException extends SystemError {

    public UserServiceUnavailableException(Throwable cause) {
        super("유저 서비스에 연결할 수 없습니다.", cause);
    }
}
