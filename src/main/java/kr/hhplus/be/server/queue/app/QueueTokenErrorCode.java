package kr.hhplus.be.server.queue.app;

import kr.hhplus.be.server.common.app.ApiErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum QueueTokenErrorCode implements ApiErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND,"TOK-404", "token.error.not_found"),
    TOKEN_EXPIRED(HttpStatus.GONE, "TOK-410", "token.error.token_expired"),
    ;

    private final HttpStatus status;
    private final String code;
    private final String messageKey;

}
