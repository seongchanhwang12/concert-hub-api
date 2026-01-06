package kr.hhplus.be.server.wallet.app;

import kr.hhplus.be.server.common.app.ApiErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum WalletErrorCode implements ApiErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND,"WAL-404", "wallet.error.not_found"),
    ;

    private final HttpStatus status;
    private final String code;
    private final String messageKey;
}
