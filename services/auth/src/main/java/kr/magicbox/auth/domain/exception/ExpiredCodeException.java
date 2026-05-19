package kr.magicbox.auth.domain.exception;

@SuppressWarnings("java:S110")
public class ExpiredCodeException extends InvalidFieldException {
    public ExpiredCodeException() {
        super("만료된 코드입니다.");
    }
}