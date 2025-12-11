package kr.hhplus.be.server.common.domain.exception;

import kr.hhplus.be.server.common.app.ApiErrorCode;

public class PolicyViolationException extends DomainException{

    public PolicyViolationException(ApiErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public PolicyViolationException(ApiErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public PolicyViolationException(ApiErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
