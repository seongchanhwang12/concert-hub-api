package kr.hhplus.be.server.reservation.api;

import kr.hhplus.be.server.concert.api.schedule.SeatResponse;
import kr.hhplus.be.server.concert.domain.schedule.Schedule;
import kr.hhplus.be.server.reservation.domain.Reservation;
import kr.hhplus.be.server.reservation.domain.ReservationSeat;

public record ReservationResponse(String ReservationId,
                                  String scheduleId,
                                  SeatResponse seats,
                                  String userid,
                                  long totalAmount,
                                  String showDate,
                                  String showTime) {

    public static ReservationResponse of(ReservationSeat reservationSeat) {
        Reservation reservation = reservationSeat.reservation();
        Schedule schedule = reservationSeat.schedule();
        SeatResponse seatsResponse = SeatResponse.from(reservationSeat.seat());

        return new ReservationResponse(
                reservation.getId().toString(),
                schedule.getId().toString(),
                seatsResponse,
                reservation.getUserId().toString(),
                reservation.getAmount().value(),
                schedule.getShowAt().toLocalDate().toString(),
                schedule.getShowAt().toLocalTime().toString());


    }
}
