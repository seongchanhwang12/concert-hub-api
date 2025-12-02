package kr.hhplus.be.server.concert.domain.schedule;

import kr.hhplus.be.server.concert.domain.concert.ConcertId;

import java.util.List;

public interface ScheduleRepository {

    List<Schedule> findSchedulesByConcertId(ConcertId concertId);

    void saveAll(List<Schedule> listSchedule);
}
