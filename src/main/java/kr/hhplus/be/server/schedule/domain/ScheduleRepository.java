package kr.hhplus.be.server.schedule.domain;

import kr.hhplus.be.server.concert.domain.ConcertId;

import java.util.List;

public interface ScheduleRepository {

    List<Schedule> findSchedulesByConcertId(ConcertId concertId);

    void saveAll(List<Schedule> listSchedule);
}
