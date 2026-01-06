package kr.hhplus.be.server.concert.domain.schedule;

import kr.hhplus.be.server.concert.domain.concert.ConcertId;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {

    Schedules findSchedulesByConcertId(ConcertId concertId);

    void saveAll(List<Schedule> listSchedule);

    Optional<Schedule> findById(ScheduleId scheduleId);

    Schedule save(Schedule schedule);
}
