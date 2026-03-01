package kr.hhplus.be.server.concert.domain.concert;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Concert {
    private ConcertId id;
    private String title;
    private LocalDate startAt;
    private LocalDate endAt;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Concert(String title, LocalDate startAt, LocalDate endAt, String description, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Concert of(String title, LocalDate startAt, LocalDate endAt, String description, LocalDateTime createdAt){
        return new Concert(title,startAt,endAt,description,createdAt,createdAt);
    }
}
