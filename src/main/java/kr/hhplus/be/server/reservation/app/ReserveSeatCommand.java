package kr.hhplus.be.server.reservation.app;


import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.concert.domain.seat.SeatId;

public record ReserveSeatCommand(UserId userId , SeatId seatId) {

    public static ReserveSeatCommand of(UserId userId, SeatId seatId) {
        return new ReserveSeatCommand(userId, seatId);
    }
}
