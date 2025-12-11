package kr.hhplus.be.server.concert.domain.seat.exception;

import kr.hhplus.be.server.common.app.ApiErrorCode;

public class SeatHoldExpiredException extends RuntimeException {

    private final ApiErrorCode errorCode;

    public SeatHoldExpiredException(ApiErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
