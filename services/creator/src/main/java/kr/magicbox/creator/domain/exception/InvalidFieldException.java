package kr.magicbox.creator.domain.exception;

<<<<<<< HEAD
import kr.magicbox.creator.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidFieldException extends BaseException {
=======
import kr.magicbox.creator.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidFieldException extends BaseException {
>>>>>>> origin/feat/100

    public InvalidFieldException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
