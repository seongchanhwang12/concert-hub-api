package kr.hhplus.be.server.reservation.infra;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaReservationRepository extends JpaRepository<JpaReservation, Long> {
}
