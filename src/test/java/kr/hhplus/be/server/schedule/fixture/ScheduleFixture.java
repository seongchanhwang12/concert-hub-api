package kr.hhplus.be.server.schedule.fixture;

import kr.hhplus.be.server.concert.domain.concert.ConcertId;
import kr.hhplus.be.server.concert.domain.schedule.Schedule;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;

import java.time.LocalDateTime;
import java.util.List;

public class ScheduleFixture {

    public static Schedule createSchedule(ConcertId concertId ){
        LocalDateTime showAt = LocalDateTime.now();
        return Schedule.of(concertId, showAt);
    }

    public static Schedule createScheduleWithId(ConcertId concertId, ScheduleId scheduleId ){
        LocalDateTime showAt = LocalDateTime.now();
        return new Schedule(scheduleId, concertId, showAt);
    }

    public static  List<Schedule> createListSchedule(ConcertId concertId){
        return List.of(createSchedule(concertId)) ;
    }

    public static  List<Schedule> createSchedulesWithId(ConcertId concertId, ScheduleId scheduleId){
        return List.of(createScheduleWithId(concertId,scheduleId)) ;
    }
}
