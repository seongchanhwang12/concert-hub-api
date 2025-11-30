package kr.hhplus.be.server.schedule.domain;

import kr.hhplus.be.server.concert.domain.ConcertId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    private ScheduleId id;
    private ConcertId concertId;
    private LocalDateTime showAt;

    public static Schedule of(ConcertId concertId, LocalDateTime showAt) {
        return new Schedule(null, concertId, showAt);
    }

}
