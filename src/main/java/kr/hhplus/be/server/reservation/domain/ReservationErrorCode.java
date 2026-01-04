package kr.hhplus.be.server.reservation.domain;

import kr.hhplus.be.server.common.app.ApiErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReservationErrorCode implements ApiErrorCode {
    SEAT_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "RSV-400", "error.reservation.limit_exceeded"),
    NOT_FOUND(HttpStatus.NOT_FOUND,"RSV-404","error.reservation.not_found" );

    private final HttpStatus status;
    private final String code;
    private final String messageKey;

}
