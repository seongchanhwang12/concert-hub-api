package kr.hhplus.be.server.concert.domain.seat;

import kr.hhplus.be.server.common.domain.Money;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import lombok.*;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Builder(access = AccessLevel.PROTECTED)
public class Seat {
    private final SeatId id;
    private final ScheduleId scheduleId;
    private final SeatNumber number;
    private final SeatGrade grade;
    private final Money price;
    private SeatStatus status;
    private LocalDateTime expiredAt;
    private LocalDateTime reservedAt;

    public static Seat of(SeatId seatId, ScheduleId scheduleId, SeatNumber seatNumber, SeatGrade seatGrade, SeatStatus seatStatus, Money price, LocalDateTime expiredAt, LocalDateTime reservedAt) {
        return Seat.builder()
                .id(seatId)
                .scheduleId(scheduleId)
                .number(seatNumber)
                .grade(seatGrade)
                .status(seatStatus)
                .price(price)
                .expiredAt(expiredAt)
                .reservedAt(reservedAt)
                .build();
    }

    /**
     * 좌석 만료 여부 확인
     * @param clock - 현재 시간
     * @return boolean - 만료 여부
     */
    public boolean isHoldExpired(Clock clock) {
        return expiredAt.isBefore(LocalDateTime.now(clock));
    }

    /**
     * 좌석 홀드 처리(5분)
     * @param now
     */
    public void hold(LocalDateTime now) {
        this.expiredAt = now.plusSeconds(60*5);
        this.status = SeatStatus.HOLD;
    }

    /**
     * 예약 가능여부 확인
     * @return
     */
    public boolean isReservable() {
        return status == SeatStatus.AVAILABLE;
    }

    public void reserve(LocalDateTime now) {
        status = SeatStatus.RESERVED;
        this.reservedAt = now;
    }

    public void assertHoldAlive(Clock clock) {
        if (status != SeatStatus.HOLD) {
            throw new IllegalStateException("Seat is not in HOLD. seatId=" + id.value());
        }

        if (expiredAt == null) {
            throw new IllegalStateException("Seat hold expiredAt is null. seatId=" + id.value());
        }

        LocalDateTime now = LocalDateTime.now(clock);
        if (!expiredAt.isAfter(now)) { // now와 같아도 만료로 처리(보수적으로)
            throw new IllegalStateException("Seat hold expired. seatId=" + id.value()
                    + ", expiredAt=" + expiredAt + ", now=" + now);
        }
    }

}
