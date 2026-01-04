package kr.hhplus.be.server.common.app;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ApiErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMM-404","error.common.not_found");
    private final HttpStatus status;
    private final String code;
    private final String messageKey;
}
