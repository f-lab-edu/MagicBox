package kr.magicbox.auth.domain.exception;

public class ExpiredRefreshTokenException extends InvalidFieldException {
    public ExpiredRefreshTokenException() {
        super("만료된 리프레시 토큰입니다.");
    }
}