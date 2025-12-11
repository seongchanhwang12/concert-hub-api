package kr.hhplus.be.server.concert.domain.seat;

import kr.hhplus.be.server.common.domain.Money;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import lombok.*;

import java.time.Clock;
import java.time.Instant;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Builder(access = AccessLevel.PROTECTED)
public class Seat {
    private final SeatId id;
    private final ScheduleId scheduleId;
    private final SeatNumber number;
    private final SeatGrade grade;
    private SeatStatus status;
    private final Money price;
    private Instant expiredAt;

    public static Seat of(ScheduleId scheduleId, SeatNumber seatNumber, SeatGrade seatGrade, SeatStatus seatStatus, Money price, Instant now) {
        return Seat.builder()
                .scheduleId(scheduleId)
                .number(seatNumber)
                .grade(seatGrade)
                .status(seatStatus)
                .price(price)
                .build();
    }


    /**
     * 좌석 만료 여부 확인
     * @param clock - 현재 시간
     * @return boolean - 만료 여부
     */
    public boolean isHoldExpired(Clock clock) {
        return expiredAt.isBefore(Instant.now(clock));
    }

    /**
     * 좌석 홀드
     * 현재시간으로부터 5분간 좌석을 HOLD 상태로 유지
     * @param clock - 현재 시간
     */
    public void hold(Clock clock) {
        this.expiredAt = Instant.now(clock).plusSeconds(60*5);
        this.status = SeatStatus.HOLD;
    }

    /**
     * 예약 가능여부 확인
     * @return
     */
    public boolean isReservable() {
        return status == SeatStatus.AVAILABLE;
    }
}
