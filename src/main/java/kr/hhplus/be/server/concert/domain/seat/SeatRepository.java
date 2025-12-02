package kr.hhplus.be.server.concert.domain.seat;

import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;

public interface SeatRepository {
    Seats findAvailableSeats(ScheduleId scheduleId);
}
