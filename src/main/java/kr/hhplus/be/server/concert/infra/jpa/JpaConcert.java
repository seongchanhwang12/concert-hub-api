package kr.hhplus.be.server.concert.infra.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "concerts")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor()
public class JpaConcert {

    @Id
    private UUID id;
    private String title;
    private LocalDate startAt;
    private LocalDate endAt;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
