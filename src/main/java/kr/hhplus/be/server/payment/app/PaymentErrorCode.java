package kr.hhplus.be.server.payment.app;

import kr.hhplus.be.server.common.app.ApiErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PaymentErrorCode implements ApiErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND,"PAY-404", "seat.error.not_found"),
    ;

    private final HttpStatus status;
    private final String code;
    private final String messageKey;
}
