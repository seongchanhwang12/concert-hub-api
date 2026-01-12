package kr.hhplus.be.server.reservation.domain;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    Optional<Reservation> findById(ReservationId reservationId);
}
