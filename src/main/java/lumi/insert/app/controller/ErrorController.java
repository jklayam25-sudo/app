package lumi.insert.app.controller;

import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lumi.insert.app.controller.wrapper.WebResponse;
import lumi.insert.app.exception.BoilerplateRequestException;
import lumi.insert.app.exception.DuplicateEntityException;
import lumi.insert.app.exception.ForbiddenRequestException;
import lumi.insert.app.exception.NotFoundEntityException;
import lumi.insert.app.exception.TransactionValidationException;

@RestControllerAdvice
public class ErrorController {
    
    @ExceptionHandler(NotFoundEntityException.class)
    public ResponseEntity<WebResponse<String>> notFoundException(NotFoundEntityException exception){
        WebResponse<String> webResponse = WebResponse.<String>builder()
        .errors(exception.getLocalizedMessage())
        .build();

        ResponseEntity<WebResponse<String>> response = ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(webResponse);

        return response;
    }

    @ExceptionHandler(BoilerplateRequestException.class)
    public ResponseEntity<WebResponse<String>> boilerplateRequestException(BoilerplateRequestException exception){
        WebResponse<String> webResponse = WebResponse.<String>builder()
        .errors(exception.getLocalizedMessage())
        .build();

        ResponseEntity<WebResponse<String>> response = ResponseEntity
        .status(HttpStatus.NOT_IMPLEMENTED)
        .body(webResponse);

        return response;
    }

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<WebResponse<String>> duplicateEntityException(DuplicateEntityException exception){
        WebResponse<String> webResponse = WebResponse.<String>builder()
        .errors(exception.getLocalizedMessage())
        .build();

        ResponseEntity<WebResponse<String>> response = ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(webResponse);

        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<WebResponse<String>> methodArgumentNotValidException(MethodArgumentNotValidException exception){
        WebResponse<String> webResponse = WebResponse.<String>builder()
        .errors(exception.getBindingResult().getAllErrors().get(0).getDefaultMessage())
        .build();

        ResponseEntity<WebResponse<String>> response = ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(webResponse);

        return response;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<WebResponse<String>> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception){
        WebResponse<String> webResponse = WebResponse.<String>builder()
        .errors(exception.getParameter().getParameterName() + " must be " + exception.getRequiredType().getSimpleName())
        .build();

        ResponseEntity<WebResponse<String>> response = ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(webResponse);

        return response;
    }

    @ExceptionHandler(ForbiddenRequestException.class)
    public ResponseEntity<WebResponse<String>> forbiddenRequestException(ForbiddenRequestException exception){
        WebResponse<String> webResponse = WebResponse.<String>builder()
        .errors(exception.getLocalizedMessage())
        .build();

        ResponseEntity<WebResponse<String>> response = ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(webResponse);

        return response;
    }

    @ExceptionHandler(TransactionValidationException.class)
    public ResponseEntity<WebResponse<String>> transactionValidationException(TransactionValidationException exception){
        WebResponse<String> webResponse = WebResponse.<String>builder()
        .errors(exception.getLocalizedMessage())
        .build();

        ResponseEntity<WebResponse<String>> response = ResponseEntity
        .status(HttpStatus.UNPROCESSABLE_CONTENT)
        .body(webResponse);

        return response;
    }
}
