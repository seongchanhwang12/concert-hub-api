package kr.hhplus.be.server.concert.infra.schedule.adapter;

import kr.hhplus.be.server.concert.domain.concert.ConcertId;
import kr.hhplus.be.server.concert.domain.schedule.Schedule;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleRepository;
import kr.hhplus.be.server.concert.domain.schedule.Schedules;
import kr.hhplus.be.server.concert.infra.schedule.jpa.JpaSchedule;
import kr.hhplus.be.server.concert.infra.schedule.jpa.JpaScheduleRepository;
import kr.hhplus.be.server.concert.infra.schedule.mapping.ScheduleEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryAdapter implements ScheduleRepository {
    private final JpaScheduleRepository scheduleRepository;
    private final ScheduleEntityMapper scheduleEntityMapper;

    @Override
    public Schedules findSchedulesByConcertId(ConcertId concertId) {
        return Schedules.of(scheduleRepository.findAllByConcertId(concertId.value()).stream()
                .map(scheduleEntityMapper::toDomain)
                .toList());
    }

    @Override
    public void saveAll(List<Schedule> listSchedule) {
        List<JpaSchedule> entities = scheduleEntityMapper.toEntities(listSchedule);
        scheduleRepository.saveAll(entities);
    }

    @Override
    public Optional<Schedule> findById(ScheduleId scheduleId) {
        return scheduleRepository.findById(scheduleId.value()).stream()
                .map(scheduleEntityMapper::toDomain)
                .findAny();
    }
}
