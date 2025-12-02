package kr.hhplus.be.server.concert.infra.seat.adapter;

import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.concert.domain.seat.SeatRepository;
import kr.hhplus.be.server.concert.domain.seat.Seats;
import kr.hhplus.be.server.concert.infra.seat.jpa.JpaSeat;
import kr.hhplus.be.server.concert.infra.seat.mapping.SeatEntityMapper;
import kr.hhplus.be.server.concert.infra.seat.query.SeatQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SeatRepositoryAdapter implements SeatRepository {

    private final SeatQueryRepository seatQueryRepository;
    private final SeatEntityMapper seatEntityMapper;

    @Override
    public Seats findAvailableSeats(ScheduleId scheduleId) {
        List<JpaSeat> availableSeats = seatQueryRepository.findAvailableSeats(scheduleId);
        return new Seats(seatEntityMapper.toDomains(availableSeats));
    }
}
