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
import java.util.Objects;

@Getter
@Builder
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

    public void assertOwnedBy(UserId userId) {
        if(!Objects.equals(this.userId, userId)) {
            throw new IllegalStateException("Owner of reservation has not been assigned to user.");
        }
    }
}
