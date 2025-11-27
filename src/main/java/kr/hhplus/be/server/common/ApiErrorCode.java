package kr.hhplus.be.server.common;

import org.springframework.http.HttpStatus;

public interface ApiErrorCode {
    HttpStatus getStatus();
    String getCode();
    String getMessageKey();

}
