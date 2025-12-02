package kr.hhplus.be.server.common.app;

import org.springframework.http.HttpStatus;

public interface ApiErrorCode {
    HttpStatus getStatus();
    String getCode();
    String getMessageKey();

}
