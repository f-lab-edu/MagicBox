package kr.magicbox.waiting.domain.exception;

public class ActiveReleaseRedisSyncException extends RuntimeException {

    public ActiveReleaseRedisSyncException(String message, Throwable cause) {
        super(message, cause);
    }
}
