package kr.hhplus.be.server.schedule.infra.jpa;

import kr.hhplus.be.server.concert.domain.ConcertId;
import kr.hhplus.be.server.schedule.domain.Schedule;
import kr.hhplus.be.server.schedule.domain.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaScheduleRepositoryAdapter implements ScheduleRepository {
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
