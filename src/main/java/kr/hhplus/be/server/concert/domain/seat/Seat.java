package kr.hhplus.be.server.concert.domain.seat;

import kr.hhplus.be.server.common.domain.Money;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Seat {
    private final SeatId id;
    private final ScheduleId scheduleId;
    private final SeatNumber number;
    private final SeatGrade grade;
    private final SeatStatus status;
    private final Money price;


}
