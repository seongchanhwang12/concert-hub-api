package kr.hhplus.be.server.seat.fixture;

import kr.hhplus.be.server.common.domain.Money;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.concert.domain.seat.*;

import java.util.List;

public class SeatFixture {
    public static Seat createSeat(){
        SeatId seatId = SeatId.of(1L);
        ScheduleId scheduleId = ScheduleId.of(1L);
        SeatNumber seatNumber = new SeatNumber(1);
        SeatStatus seatStatus = SeatStatus.AVAILABLE;
        SeatGrade seatGrade = SeatGrade.S;
        Money price = Money.wons(15000);
        return new Seat(seatId, scheduleId, seatNumber, seatGrade, seatStatus, price);
    }

    public static Seats createSeats(){
        Seat seat = createSeat();
        return new Seats(List.of(seat));
    }

}
