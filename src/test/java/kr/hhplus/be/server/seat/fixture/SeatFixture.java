package kr.hhplus.be.server.seat.fixture;

import java.time.Clock;
import kr.hhplus.be.server.common.domain.Money;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.concert.domain.seat.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public class SeatFixture {

    public static Seat createHoldSeat(SeatId seatId, SeatStatus seatStatus, LocalDateTime now) {
        ScheduleId scheduleId = ScheduleId.of(1L);
        SeatNumber seatNumber = new SeatNumber(1);
        SeatGrade seatGrade = SeatGrade.S;
        Money price = Money.wons(15000);
        Seat seat = Seat.of(seatId, scheduleId, seatNumber, seatGrade, seatStatus, price, now,now);
        seat.hold(now);
        return seat;
    }
    public static Seat createSeat(SeatStatus seatStatus) {
        SeatId seatId = SeatId.of(1L);
        ScheduleId scheduleId = ScheduleId.of(1L);
        SeatNumber seatNumber = new SeatNumber(1);
        SeatGrade seatGrade = SeatGrade.S;
        Money price = Money.wons(15000);
        return Seat.of(seatId, scheduleId, seatNumber, seatGrade, seatStatus, price, LocalDateTime.now(),LocalDateTime.now());
    }

    public static Seat create(SeatStatus seatStatus) {
        ScheduleId scheduleId = ScheduleId.of(1L);
        SeatNumber seatNumber = new SeatNumber(1);
        SeatGrade seatGrade = SeatGrade.S;
        Money price = Money.wons(15000);
        SeatId seatId = SeatId.of(1L);
        return Seat.of(seatId, scheduleId, seatNumber, seatGrade, seatStatus, price, LocalDateTime.now(),LocalDateTime.now());
    }

    public static Seats createSeats(SeatStatus seatStatus) {
        Seat seat = createSeat(seatStatus);
        return new Seats(List.of(seat));
    }

}
