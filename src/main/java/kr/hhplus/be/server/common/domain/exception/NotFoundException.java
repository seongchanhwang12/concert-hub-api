package kr.hhplus.be.server.common.domain.exception;

import kr.hhplus.be.server.common.app.ApiErrorCode;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final ApiErrorCode errorCode;

    public NotFoundException(ApiErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
