package kr.hhplus.be.server.reservation.domain;

import kr.hhplus.be.server.common.domain.Money;
import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.concert.domain.seat.Seat;
import kr.hhplus.be.server.concert.domain.seat.SeatId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Reservation {

    private final ReservationId id;
    private final UserId userId;
    private final SeatId seatId;
    private final ScheduleId scheduleId;
    private final Money amount;
    private ReservationStatus status;
    private Instant createdAt;

        public static Reservation createConfirmed(ReservationId id, Seat seat, UserId userId) {
            return new Reservation(id,
                    userId,
                    seat.getId(),
                    seat.getScheduleId(),
                    seat.getPrice(),
                    ReservationStatus.CONFIRMED,
                    Instant.now());
    }

}
