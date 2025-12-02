package kr.hhplus.be.server.concert.domain.seat;

import java.util.List;

public record Seats(List<Seat> values) {

    public static Seats empty() {
        return new Seats(List.of());
    }
}
