package kr.hhplus.be.server.reservation.domain;

import kr.hhplus.be.server.common.domain.Money;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.concert.domain.seat.SeatId;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationFixture {

    public static Reservation createConfirmedWith (SeatId seatId, ScheduleId scheduleId) {
        return Reservation.builder()
                .seatId(seatId)
                .scheduleId(scheduleId)
                .amount(Money.wons(10_000L))
                .status(ReservationStatus.CONFIRMED)
                .build();
    }

}