package kr.hhplus.be.server.concert.api.concert;

import kr.hhplus.be.server.concert.api.schedule.ScheduleResponse;
import kr.hhplus.be.server.concert.domain.concert.Concert;

import java.time.LocalDate;
import java.util.List;

public record GetConcertResponse(long concertId,
                                 String title,
                                 LocalDate startAt,
                                 LocalDate endAt,
                                 String description,
                                 List<ScheduleResponse> scheduleSummaries) {

    public static GetConcertResponse from(Concert concert, List<ScheduleResponse> schedules) {
        return new GetConcertResponse(
                concert.getId().value(),
                concert.getTitle(),
                concert.getStartAt(),
                concert.getEndAt(),
                concert.getDescription(),
                schedules
                );
    }
}
