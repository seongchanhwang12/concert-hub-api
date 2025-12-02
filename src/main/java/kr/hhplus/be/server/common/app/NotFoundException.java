package kr.hhplus.be.server.common.app;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final ApiErrorCode errorCode;

    public NotFoundException(ApiErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
