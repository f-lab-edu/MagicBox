package kr.magicbox.auth.domain.exception;

public class RevokedRefreshTokenException extends InvalidFieldException {
    public RevokedRefreshTokenException() {
        super("취소된 리프레시 토큰입니다.");
    }
}