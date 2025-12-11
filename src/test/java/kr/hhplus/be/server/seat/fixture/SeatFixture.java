package kr.hhplus.be.server.seat.fixture;

import kr.hhplus.be.server.common.domain.Money;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.concert.domain.seat.*;

import java.time.Instant;
import java.util.List;

public class SeatFixture {
    public static Seat createSeat(SeatStatus seatStatus) {
        SeatId seatId = SeatId.of(1L);
        ScheduleId scheduleId = ScheduleId.of(1L);
        SeatNumber seatNumber = new SeatNumber(1);
        SeatGrade seatGrade = SeatGrade.S;
        Money price = Money.wons(15000);
        return new Seat(seatId, scheduleId, seatNumber, seatGrade, seatStatus, price, Instant.now());
    }

    public static Seat create(SeatStatus seatStatus) {
        ScheduleId scheduleId = ScheduleId.of(1L);
        SeatNumber seatNumber = new SeatNumber(1);
        SeatGrade seatGrade = SeatGrade.S;
        Money price = Money.wons(15000);
        return Seat.of(scheduleId, seatNumber, seatGrade, seatStatus, price, Instant.now());
    }

    public static Seats createSeats(SeatStatus seatStatus) {
        Seat seat = createSeat(seatStatus);
        return new Seats(List.of(seat));
    }

}
