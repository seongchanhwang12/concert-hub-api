package kr.hhplus.be.server.common.api;

import kr.hhplus.be.server.common.app.ApiErrorCode;
import kr.hhplus.be.server.common.domain.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(ApplicationException e) {
        log.error(e.getMessage(), e);
        ApiErrorCode errorCode = e.getErrorCode();

        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.getStatus().value()
                ,errorCode.getCode(),
                errorCode.getMessageKey());

        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);

    }

}
