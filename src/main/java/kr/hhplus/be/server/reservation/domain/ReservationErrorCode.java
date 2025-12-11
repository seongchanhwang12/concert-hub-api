package kr.hhplus.be.server.reservation.domain;

import kr.hhplus.be.server.common.app.ApiErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReservationErrorCode implements ApiErrorCode {
    SEAT_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "RSV_400", "error.reservation.limit_exceeded");

    private final HttpStatus status;
    private final String code;
    private final String messageKey;

}
