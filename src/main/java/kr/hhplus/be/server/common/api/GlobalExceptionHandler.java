package kr.hhplus.be.server.common.api;

import kr.hhplus.be.server.common.app.ApiErrorCode;
import kr.hhplus.be.server.common.app.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        log.error(e.getMessage(), e);
        ApiErrorCode errorCode = e.getErrorCode();

        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.getStatus().value()
                ,errorCode.getCode(),
                errorCode.getMessageKey());

        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);

    }

}
