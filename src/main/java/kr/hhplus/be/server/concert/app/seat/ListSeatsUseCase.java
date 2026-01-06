package kr.hhplus.be.server.concert.app.seat;

import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.concert.domain.seat.SeatRepository;
import kr.hhplus.be.server.concert.domain.seat.Seats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListSeatsUseCase {

    private final SeatRepository seatRepository;

    public Seats listSeats(ScheduleId scheduleId) {
        return seatRepository.findAvailableSeats(scheduleId);
    }
}
