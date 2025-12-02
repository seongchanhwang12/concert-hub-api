package kr.hhplus.be.server.concert.domain.concert;


import kr.hhplus.be.server.concert.domain.schedule.Schedules;

public record ConcertDetail(Concert concert, Schedules schedules) {

    public static ConcertDetail from(Concert concert, Schedules schedules) {
        return new ConcertDetail(concert, schedules);
    }
}
