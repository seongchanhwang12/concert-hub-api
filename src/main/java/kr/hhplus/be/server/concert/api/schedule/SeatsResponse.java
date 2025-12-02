package kr.hhplus.be.server.concert.api.schedule;

import kr.hhplus.be.server.concert.domain.seat.Seats;

import java.util.List;

public record SeatsResponse(List<SeatResponse> seats) {

    public static SeatsResponse from(Seats seats) {
        return new SeatsResponse(seats.values().stream()
                .map(SeatResponse::from)
                .toList());

    }
}
