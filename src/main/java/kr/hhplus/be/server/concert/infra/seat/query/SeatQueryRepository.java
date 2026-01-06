package kr.hhplus.be.server.concert.infra.seat.query;

import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.concert.infra.seat.jpa.JpaSeat;

import java.util.List;

public interface SeatQueryRepository {
    List<JpaSeat> findAvailableSeats(ScheduleId scheduleId);
}
