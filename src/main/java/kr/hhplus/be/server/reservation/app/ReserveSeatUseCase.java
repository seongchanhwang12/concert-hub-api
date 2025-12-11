package kr.hhplus.be.server.reservation.app;

import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.common.domain.exception.NotFoundException;
import kr.hhplus.be.server.concert.app.schedule.ScheduleQueryService;
import kr.hhplus.be.server.concert.domain.schedule.Schedule;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleRepository;
import kr.hhplus.be.server.concert.domain.seat.Seat;
import kr.hhplus.be.server.concert.domain.seat.SeatId;
import kr.hhplus.be.server.concert.domain.seat.SeatIds;
import kr.hhplus.be.server.concert.domain.seat.SeatRepository;
import kr.hhplus.be.server.concert.domain.seat.exception.SeatErrorCode;
import kr.hhplus.be.server.concert.domain.seat.exception.SeatException;
import kr.hhplus.be.server.reservation.domain.Reservation;
import kr.hhplus.be.server.reservation.domain.ReservationId;
import kr.hhplus.be.server.reservation.domain.ReservationRepository;
import kr.hhplus.be.server.reservation.domain.ReservationSeat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReserveSeatUseCase {
    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;
    private final ScheduleQueryService scheduleQueryService;
    private final IdGenerator idGenerator;
    private final Clock clock;

    /**
     * 좌석 예약 메서드
     * Seat 의 상태를 hold 로 변경하고 예약 정보롤 저장 후 반환한다.
     * Seat hold 시 만료시간을 Seat 객체에 세팅한다.
     * Redis 적용 전까지 임시적으로 빈관적 락 정책을 사용한다.
     *
     * @param reserveSeatCommand - 사용자 ID
     * @return Reservation - 예약 정보
     */
    @Transactional
    public ReservationSeat reserveSeat(ReserveSeatCommand reserveSeatCommand) {
        final SeatId seatId = reserveSeatCommand.seatId();
        final UserId userId = reserveSeatCommand.userId();

        final Seat seat = seatRepository.find(seatId)
                .orElseThrow(() -> new NotFoundException(SeatErrorCode.NOT_FOUND, "seat not found. seatId = " + seatId.value()));

        // Seat 상태가 AVAILABLE 이 아닌 경우 예외
        if(!seat.isReservable())
            throw new SeatException(SeatErrorCode.NOT_AVAILABLE, "seat status is not available. status = " + seat.getStatus());

        final Schedule schedule = scheduleQueryService.findScheduleById(seat.getScheduleId());

        // 시트 상태 변경
        seat.hold(clock);
        seatRepository.save(seat);

        final Reservation save = reservationRepository.save(Reservation.createConfirmed(ReservationId.of(idGenerator.nextId()), seat, userId));

        // 예매하기
        return ReservationSeat.of(save, schedule, seat);


    }


}
