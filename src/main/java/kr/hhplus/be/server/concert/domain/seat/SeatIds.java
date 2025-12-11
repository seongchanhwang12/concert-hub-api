package kr.hhplus.be.server.concert.domain.seat;

import java.util.Iterator;
import java.util.List;

public record SeatIds(List<SeatId> values ) implements Iterable<SeatId> {

    @Override
    public Iterator<SeatId> iterator() {
        return values.iterator();
    }
}
