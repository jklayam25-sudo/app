package lumi.insert.app.exception;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class AuthenticationTokenException extends AuthenticationException{
    public AuthenticationTokenException(@Nullable String message) {
        super(message); 
    }
    
}
