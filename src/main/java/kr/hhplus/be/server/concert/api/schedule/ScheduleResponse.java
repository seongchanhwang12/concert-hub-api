package kr.hhplus.be.server.concert.api.schedule;

import kr.hhplus.be.server.concert.domain.schedule.Schedule;

public record ScheduleResponse(Long id, String showDate, String showTime) {

    public static ScheduleResponse from(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId().value(),
                schedule.getShowAt().toLocalDate().toString(),
                schedule.getShowAt().toLocalTime().toString()
        );
    }


}
