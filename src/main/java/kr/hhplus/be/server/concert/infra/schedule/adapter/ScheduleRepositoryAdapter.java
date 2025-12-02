package kr.hhplus.be.server.concert.infra.schedule.adapter;

import kr.hhplus.be.server.concert.domain.concert.ConcertId;
import kr.hhplus.be.server.concert.domain.schedule.Schedule;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleRepository;
import kr.hhplus.be.server.concert.infra.schedule.jpa.JpaSchedule;
import kr.hhplus.be.server.concert.infra.schedule.jpa.JpaScheduleRepository;
import kr.hhplus.be.server.concert.infra.schedule.mapping.ScheduleEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryAdapter implements ScheduleRepository {
    private final JpaScheduleRepository jpa;
    private final ScheduleEntityMapper scheduleEntityMapper;

    @Override
    public List<Schedule> findSchedulesByConcertId(ConcertId concertId) {
        return jpa.findAllByConcertId(concertId.value()).stream()
                .map(scheduleEntityMapper::toDomain)
                .toList();
    }

    @Override
    public void saveAll(List<Schedule> listSchedule) {
        List<JpaSchedule> entities = scheduleEntityMapper.toEntities(listSchedule);
        jpa.saveAll(entities);
    }
}
