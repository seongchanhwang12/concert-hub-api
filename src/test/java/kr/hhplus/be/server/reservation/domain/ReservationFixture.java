package kr.hhplus.be.server.reservation.domain;

import kr.hhplus.be.server.common.domain.Money;
import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.concert.domain.seat.SeatId;

import java.util.UUID;

public class ReservationFixture {


    public static Reservation createConfirmedWith (SeatId seatId, ScheduleId scheduleId) {
        return Reservation.builder()
                .seatId(seatId)
                .scheduleId(scheduleId)
                .amount(Money.wons(10_000L))
                .status(ReservationStatus.CONFIRMED)
                .build();
    }

    public static Reservation createConfirmedWith(Money money, UserId userId) {
        SeatId seatId = SeatId.of(1L);
        ScheduleId scheduleId = ScheduleId.of(1L);
        ReservationId reservationId = ReservationId.of(UUID.randomUUID());
        return Reservation.builder()
                .id(reservationId)
                .seatId(seatId)
                .scheduleId(scheduleId)
                .amount(money)
                .userId(userId)
                .status(ReservationStatus.CONFIRMED)
                .build();
    }

}