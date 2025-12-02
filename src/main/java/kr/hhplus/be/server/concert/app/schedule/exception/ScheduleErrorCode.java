package kr.hhplus.be.server.concert.app.schedule.exception;

import kr.hhplus.be.server.common.app.ApiErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ScheduleErrorCode implements ApiErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "SCH-404","error.schedule.not_found");
    private final HttpStatus status;
    private final String code;
    private final String messageKey;

}
