package kr.hhplus.be.server.schedule.app;

import kr.hhplus.be.server.concert.domain.ConcertId;

import java.time.LocalDateTime;

public record ListScheduleCommand(ConcertId concertId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
}
