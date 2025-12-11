package kr.hhplus.be.server.concert.domain.seat;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public record Seats(List<Seat> values) implements Iterable<Seat> {

    public static Seats empty() {
        return new Seats(List.of());
    }

    public static Seats add(Seat... seat) {
        return new Seats(List.of(seat));
    }

    public Stream<Seat> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    public Iterator<Seat> iterator() {
        return values.iterator();
    }


}
