package kr.hhplus.be.server.schedule.domain;

public record ScheduleId(long value) {

    public ScheduleId(long value) {
        this.value = value;
    }

    public static ScheduleId of(long id) {
        return new ScheduleId(id);
    }

    @Override
    public String toString(){
        return String.valueOf(value);
    }
}
