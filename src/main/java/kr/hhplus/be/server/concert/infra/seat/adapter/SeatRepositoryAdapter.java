package kr.hhplus.be.server.concert.infra.seat.adapter;

import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.concert.domain.seat.Seat;
import kr.hhplus.be.server.concert.domain.seat.SeatId;
import kr.hhplus.be.server.concert.domain.seat.SeatRepository;
import kr.hhplus.be.server.concert.domain.seat.Seats;
import kr.hhplus.be.server.concert.infra.seat.jpa.JpaSeat;
import kr.hhplus.be.server.concert.infra.seat.jpa.JpaSeatRepository;
import kr.hhplus.be.server.concert.infra.seat.mapping.SeatEntityMapper;
import kr.hhplus.be.server.concert.infra.seat.query.SeatQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SeatRepositoryAdapter implements SeatRepository {

    private final JpaSeatRepository jpaSeatRepository;
    private final SeatQueryRepository seatQueryRepository;
    private final SeatEntityMapper seatEntityMapper;

    @Override
    public Seats findAvailableSeats(ScheduleId scheduleId) {
        List<JpaSeat> availableSeats = seatQueryRepository.findAvailableSeats(scheduleId);
        return new Seats(seatEntityMapper.toDomains(availableSeats));
    }

    @Override
    public Optional<Seat> find(SeatId seatId) {
        return jpaSeatRepository.findByIdForUpdate(seatId.value())
                .map(seatEntityMapper::toDomain);
    }

    @Override
    public Seat save(Seat seat) {
        JpaSeat saved = jpaSeatRepository.save(seatEntityMapper.toEntity(seat));
        return seatEntityMapper.toDomain(saved);
    }
}
