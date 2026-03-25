package kr.magicbox.auth.domain.exception;

public class ExpiredCodeException extends InvalidFieldException {
    public ExpiredCodeException() {
        super("만료된 코드입니다.");
    }
}