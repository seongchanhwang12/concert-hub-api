package kr.hhplus.be.server.concert.infra.seat.mapping;

import kr.hhplus.be.server.common.domain.Money;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.concert.domain.seat.*;
import kr.hhplus.be.server.concert.infra.seat.jpa.JpaSeat;
import kr.hhplus.be.server.config.mapstruct.MapstructMapperConfig;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapstructMapperConfig.class)
public interface SeatEntityMapper {

    Seat toDomain(JpaSeat jpaSeat);

    List<Seat> toDomains(List<JpaSeat> jpaSeats);

    default ScheduleId toScheduleId(Long id){
        return ScheduleId.of(id);
    };

    default SeatId toSeatId(Long id){
        return SeatId.of(id);
    };

    default SeatNumber toSeatNumber(int number){
        return new SeatNumber(number);
    }

    default SeatGrade toSeatGrade(String grade){
        return SeatGrade.valueOf(grade);
    }

    default SeatStatus toStatus(String status){
        return SeatStatus.valueOf(status);
    }

    default Money toMoney(Long price){
        return new Money(price);
    }

}
