package lumi.insert.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TransactionValidationException extends RuntimeException{
    public TransactionValidationException (String message){
        super(message);
    }
}
