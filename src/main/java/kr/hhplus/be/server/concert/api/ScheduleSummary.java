package kr.hhplus.be.server.concert.api;

import kr.hhplus.be.server.schedule.domain.Schedule;

public record ScheduleSummary(Long id, String showDate, String showTime) {

    public static ScheduleSummary from(Schedule schedule) {
        return new ScheduleSummary(
                schedule.getId().value(),
                schedule.getShowAt().toLocalDate().toString(),
                schedule.getShowAt().toLocalTime().toString()
        );
    }


}
