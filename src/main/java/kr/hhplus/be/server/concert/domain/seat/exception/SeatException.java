package kr.hhplus.be.server.concert.domain.seat.exception;

import kr.hhplus.be.server.common.app.ApiErrorCode;
import kr.hhplus.be.server.common.domain.exception.DomainException;
import lombok.Getter;

@Getter
public class SeatException extends DomainException {

    public SeatException(ApiErrorCode errorCode, String message, Throwable cause) {
        super(errorCode,message,cause);
    }

    public SeatException(ApiErrorCode errorCode, String message) {
        super(errorCode,message);
    }
}
