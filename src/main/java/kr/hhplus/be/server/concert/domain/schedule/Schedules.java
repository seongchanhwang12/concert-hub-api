package kr.hhplus.be.server.concert.domain.schedule;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Getter
public class Schedules implements Iterable<Schedule> {
    private final List<Schedule> values;

    private Schedules(List<Schedule> values) {
        this.values = List.copyOf(values);
    }

    public static Schedules of(List<Schedule> list) {
        return new Schedules(list);
    }

    public static Schedules from(Schedule schedule) {
        return Schedules.of(List.of(schedule));
    }

    public Schedules add(Schedule schedule) {
        List<Schedule> copy = new ArrayList<>(values);
        copy.add(schedule);
        return new Schedules(copy);
    }

    public long size(){
        return values.size();
    }

    @Override
    public Iterator<Schedule> iterator() {
        return values.iterator();
    }

    public Stream<Schedule> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}
