package kr.hhplus.be.server.concert.api;

import kr.hhplus.be.server.concert.domain.Concert;

import java.time.LocalDate;
import java.util.List;

public record GetConcertResponse(long concertId,
                                 String title,
                                 LocalDate startAt,
                                 LocalDate endAt,
                                 String description,
                                 List<ScheduleSummary> scheduleSummaries) {

    public static GetConcertResponse from(Concert concert, List<ScheduleSummary> schedules) {
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
