package kr.hhplus.be.server.concert.domain.seat;

public record SeatId(long value) {
    public static SeatId of(long id) {
        return new SeatId(id);
    }
}
