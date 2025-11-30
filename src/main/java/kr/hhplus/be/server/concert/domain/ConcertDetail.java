package kr.hhplus.be.server.concert.domain;


import kr.hhplus.be.server.schedule.domain.Schedule;

import java.util.List;

public record ConcertDetail(Concert concert, List<Schedule> schedules) {

    public static ConcertDetail from(Concert concert, List<Schedule> schedules) {
        return new ConcertDetail(concert, schedules);
    }
}
