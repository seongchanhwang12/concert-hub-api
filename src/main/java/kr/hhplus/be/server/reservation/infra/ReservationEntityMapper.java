package kr.hhplus.be.server.reservation.infra;

import kr.hhplus.be.server.common.domain.Money;
import kr.hhplus.be.server.common.infra.mapper.CommonValueObjectMapper;
import kr.hhplus.be.server.concert.domain.seat.SeatId;
import kr.hhplus.be.server.concert.infra.schedule.mapping.ScheduleEntityMapper;
import kr.hhplus.be.server.config.mapstruct.MapstructMapperConfig;
import kr.hhplus.be.server.reservation.domain.Reservation;
import kr.hhplus.be.server.reservation.domain.ReservationId;
import kr.hhplus.be.server.reservation.domain.ReservationStatus;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(config = MapstructMapperConfig.class,
uses = {CommonValueObjectMapper.class, ScheduleEntityMapper.class})
public interface ReservationEntityMapper {

    JpaReservation toEntity(Reservation reservation);

    Reservation toDomain(JpaReservation save);

    default UUID fromReservationId(ReservationId reservationId) {
        if(reservationId == null) return null;
        return reservationId.value();
    }

    default ReservationStatus toReservationStatus(String status) {
        return ReservationStatus.valueOf(status);
    }

    default String fromReservationStatus(ReservationStatus status) {
        return status.name();
    }

    default ReservationId toReservationId(UUID id) {
        return new ReservationId(id);
    }

    default SeatId toSeatId(long seatId) {
        return new SeatId(seatId);
    }

    default long fromSeatId(SeatId seatId) {
        return seatId.value();
    }






}
