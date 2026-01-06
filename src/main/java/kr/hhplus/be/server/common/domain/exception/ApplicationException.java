package kr.hhplus.be.server.common.domain.exception;

import kr.hhplus.be.server.common.app.ApiErrorCode;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final ApiErrorCode errorCode;

    public ApplicationException(ApiErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
