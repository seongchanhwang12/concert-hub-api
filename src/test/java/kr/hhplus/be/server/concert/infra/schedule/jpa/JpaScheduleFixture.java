package kr.hhplus.be.server.concert.infra.schedule.jpa;

import kr.hhplus.be.server.concert.domain.concert.ConcertId;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class JpaScheduleFixture {
    public static JpaSchedule create(ConcertId concertId){

        return JpaSchedule.builder()
                .concertId(concertId.value())
                .showAt(LocalDateTime.now())
                .build();

    }

}