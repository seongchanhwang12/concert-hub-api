package kr.hhplus.be.server.concert.domain.seat.exception;

import kr.hhplus.be.server.common.app.ApiErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SeatErrorCode implements ApiErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND,"SEAT-404", "seat.error.not_found"),
    HOLD_EXPIRED(HttpStatus.CONFLICT, "SEAT-409", "seat.error.hold_expired"),
    NOT_AVAILABLE(HttpStatus.CONFLICT, "SEAT-409" , "seat.error.not_available"),
    ;

    private final HttpStatus status;
    private final String code;
    private final String messageKey;
}
