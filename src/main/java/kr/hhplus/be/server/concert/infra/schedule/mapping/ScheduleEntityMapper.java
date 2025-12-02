package kr.hhplus.be.server.concert.infra.schedule.mapping;

import kr.hhplus.be.server.concert.domain.concert.ConcertId;
import kr.hhplus.be.server.concert.infra.schedule.jpa.JpaSchedule;
import kr.hhplus.be.server.config.mapstruct.MapstructMapperConfig;
import kr.hhplus.be.server.concert.domain.schedule.Schedule;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
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
