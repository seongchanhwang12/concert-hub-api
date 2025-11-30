package kr.hhplus.be.server.schedule.infra.jpa;

import kr.hhplus.be.server.concert.domain.ConcertId;
import kr.hhplus.be.server.config.mapstruct.MapstructMapperConfig;
import kr.hhplus.be.server.schedule.domain.Schedule;
import kr.hhplus.be.server.schedule.domain.ScheduleId;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapstructMapperConfig.class)
public interface ScheduleEntityMapper {
    Schedule toDomain(JpaSchedule jpaSchedule);

    List<JpaSchedule> toEntities(List<Schedule> listSchedule);

    JpaSchedule toEntity(Schedule schedule);

    default Long toScheduleIdValue(ScheduleId scheduleId){
        return scheduleId == null ? null : scheduleId.value();
    }

    default Long toConcertIdValue(ConcertId concertId){
        return concertId.value();
    }

    default ScheduleId toScheduleId(Long id){
        return ScheduleId.of(id);
    }


}
