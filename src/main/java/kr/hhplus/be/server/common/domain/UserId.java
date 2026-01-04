package kr.hhplus.be.server.common.domain;

import kr.hhplus.be.server.common.app.CommonErrorCode;
import kr.hhplus.be.server.common.domain.exception.ApplicationException;

import java.util.UUID;

public record UserId(UUID value) {

    public UserId{
        if(value == null){
            throw new ApplicationException(CommonErrorCode.NOT_FOUND, "userId is null" );
        }
    }

    public static UserId of(UUID value) {
        return new UserId(value);
    }
}
