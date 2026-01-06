package kr.hhplus.be.server.reservation.domain;

import java.util.UUID;

public record ReservationId(UUID value) {

    public static ReservationId of(UUID value) {
        return new ReservationId(value);
    }
}
