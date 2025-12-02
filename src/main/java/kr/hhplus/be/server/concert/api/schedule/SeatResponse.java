package kr.hhplus.be.server.concert.api.schedule;

import kr.hhplus.be.server.concert.domain.seat.Seat;

public record SeatResponse(
        long seatId,
        int seatNumber,
        long scheduleId,
        String status) {

    public static SeatResponse from(Seat seat) {
        return new SeatResponse(
                seat.getId().value(),
                seat.getNumber().value(),
                seat.getScheduleId().value(),
                seat.getStatus().name()
        );

    }
}
