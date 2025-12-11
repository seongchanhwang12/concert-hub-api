package kr.hhplus.be.server.common.domain.exception;

import kr.hhplus.be.server.common.app.ApiErrorCode;
import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {
    private final ApiErrorCode errorCode;

    public DomainException(ApiErrorCode errorCode, String message, Throwable cause) {
        super(message,cause);
        this.errorCode = errorCode;
    }

    public DomainException(ApiErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public DomainException(ApiErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }
}
