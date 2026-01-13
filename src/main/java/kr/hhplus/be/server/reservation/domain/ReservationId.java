package kr.hhplus.be.server.reservation.domain;

import java.util.UUID;

public record ReservationId(UUID value) {

    public ReservationId {
        if (value == null) {
            throw new IllegalArgumentException("reservationId cannot be null");
        }
    }

    public static ReservationId of(UUID value) {
        return new ReservationId(value);
    }
}
