package kr.hhplus.be.server.concert.infra.seat.jpa;

import kr.hhplus.be.server.concert.domain.seat.SeatGrade;
import kr.hhplus.be.server.concert.domain.seat.SeatStatus;

import static org.junit.jupiter.api.Assertions.*;

public class JpaSeatFixture {

    public static JpaSeat create(long scheduleId, SeatStatus seatStatus) {
        return JpaSeat.builder()
                .scheduleId(scheduleId)
                .grade(SeatGrade.VIP.name())
                .price(10000L)
                .number(10)
                .status(seatStatus.name())
                .build();
    }

}