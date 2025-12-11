package kr.hhplus.be.server.reservation.infra;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor()
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JpaReservation {

    @Id
    private UUID id;
    private UUID userId;
    private long seatId;
    private long scheduleId;
    private long amount;
    private String status;
    private Instant createdAt;

}
