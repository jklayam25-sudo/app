package lumi.insert.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
public class BoilerplateRequestException extends RuntimeException{
    public BoilerplateRequestException (String message){
        super(message);
    }
}
