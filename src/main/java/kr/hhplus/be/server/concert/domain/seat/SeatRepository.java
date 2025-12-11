package kr.hhplus.be.server.concert.domain.seat;

import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;

import java.util.Optional;

public interface SeatRepository {
    Seats findAvailableSeats(ScheduleId scheduleId);

    Optional<Seat> find(SeatId seatId);

    Seat save(Seat seat);

}
