package kr.hhplus.be.server.reservation.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaReservationRepository extends JpaRepository<JpaReservation, UUID> {
}
