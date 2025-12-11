package kr.hhplus.be.server.reservation.domain;

import kr.hhplus.be.server.concert.domain.schedule.Schedule;
import kr.hhplus.be.server.concert.domain.seat.Seat;
import kr.hhplus.be.server.concert.domain.seat.Seats;

public record ReservationSeat(
        Reservation reservation,
        Schedule schedule,
        Seat seat
) {
    public static ReservationSeat of(Reservation reservation, Schedule schedule, Seat seat) {
        return new ReservationSeat(reservation, schedule, seat);
    }
}
