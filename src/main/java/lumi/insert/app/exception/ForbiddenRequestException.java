package lumi.insert.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ForbiddenRequestException extends RuntimeException{
    public ForbiddenRequestException(String message){
        super(message);
    }
}
