package kr.hhplus.be.server.concert.domain.schedule;

import kr.hhplus.be.server.common.domain.exception.ApplicationException;
import kr.hhplus.be.server.concert.app.schedule.exception.ScheduleErrorCode;

public record ScheduleId(long value) {

    public ScheduleId {
        if(value <= 0) throw new ApplicationException(ScheduleErrorCode.NOT_FOUND,"schedule id is not valid. value [" + value +"]" );
    }

    public static ScheduleId of(long id) {
        return new ScheduleId(id);
    }

    @Override
    public String toString(){
        return String.valueOf(value);
    }
}
