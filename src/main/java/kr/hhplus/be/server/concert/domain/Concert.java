package kr.hhplus.be.server.concert.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Concert {
    private ConcertId id;
    private String title;
    private LocalDate startAt;
    private LocalDate endAt;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Concert of(String title, LocalDate startAt, LocalDate endAt, String description){
        return new Concert(null, title,startAt,endAt,description,LocalDateTime.now(),LocalDateTime.now());
    }
}
