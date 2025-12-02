package kr.hhplus.be.server.concert.app.concert.exception;

import kr.hhplus.be.server.common.app.ApiErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ConcertErrorCode implements ApiErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND,"CON_404","error.concert.not_found");

    private final HttpStatus status;
    private final String code;
    private final String messageKey;


}
