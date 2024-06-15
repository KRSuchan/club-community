package web.club_community.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import web.club_community.exception.runtime.DuplicateEmailException;

import javax.naming.AuthenticationException;
import java.util.Locale;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class ApiExceptionController {
    private final MessageSource messageSource;

    public ApiExceptionController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private String getErrorMessage(ObjectError error) {
        String[] codes = error.getCodes();
        for (String code : codes) {
            try {
                return messageSource.getMessage(code, error.getArguments(), Locale.KOREA);
            } catch (NoSuchMessageException ignored) {
            }
        }
        return error.getDefaultMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> illegalExHandler(IllegalArgumentException e) {
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        ApiErrorResponse errorResponse = ApiErrorResponse.of(errorCode, e.getMessage());
        return ResponseEntity.status(errorCode.getCode()).body(errorResponse);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> AuthenticationExHandler(AuthenticationException e) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        ApiErrorResponse errorResponse = ApiErrorResponse.of(errorCode, e.getMessage());
        return ResponseEntity.status(errorCode.getCode()).body(errorResponse);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiErrorResponse> NoSuchElementExHandler(NoSuchElementException e) {
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        ApiErrorResponse errorResponse = ApiErrorResponse.of(errorCode, e.getMessage());
        return ResponseEntity.status(errorCode.getCode()).body(errorResponse);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiErrorResponse> duplicateEmailExHandler(DuplicateEmailException e) {
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        ApiErrorResponse errorResponse = ApiErrorResponse.of(errorCode, e.getMessage());
        return ResponseEntity.status(errorCode.getCode()).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> internalExHandler(Exception e) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        ApiErrorResponse errorResponse = ApiErrorResponse.of(errorCode, e.getMessage());
        return ResponseEntity.status(errorCode.getCode()).body(errorResponse);
    }
}
