package kr.hhplus.be.server.concert.infra.seat.mapping;

import kr.hhplus.be.server.common.domain.Money;
import kr.hhplus.be.server.common.infra.mapper.CommonValueObjectMapper;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.concert.domain.seat.*;
import kr.hhplus.be.server.concert.infra.seat.jpa.JpaSeat;
import kr.hhplus.be.server.config.mapstruct.MapstructMapperConfig;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.UUID;

@Mapper(config = MapstructMapperConfig.class,
uses = {CommonValueObjectMapper.class})
public interface SeatEntityMapper {

    Seat toDomain(JpaSeat jpaSeat);

    List<Seat> toDomains(List<JpaSeat> jpaSeats);

    default ScheduleId toScheduleId(Long id){
        return ScheduleId.of(id);
    };

    default Long fromScheduleId(ScheduleId scheduleId){
        return scheduleId.value();
    }

    default SeatId toSeatId(long id){
        return SeatId.of(id);
    };

    default Long fromSeatId(SeatId seatId){
        if (seatId == null) return null;
        return seatId.value();
    }

    default SeatNumber toSeatNumber(int number){
        return new SeatNumber(number);
    }

    default int fromSeatNumber(SeatNumber seatNumber){
        return seatNumber.value();
    }

    default SeatGrade toSeatGrade(String grade){
        return SeatGrade.valueOf(grade);
    }

    default SeatStatus toStatus(String status){
        return SeatStatus.valueOf(status);
    }

    JpaSeat toEntity(Seat seat);

}
