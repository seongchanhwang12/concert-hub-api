package kr.hhplus.be.server.reservation.infra;

import kr.hhplus.be.server.reservation.domain.Reservation;
import kr.hhplus.be.server.reservation.domain.ReservationId;
import kr.hhplus.be.server.reservation.domain.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryAdapter implements ReservationRepository {

    private final JpaReservationRepository reservationRepository;
    private final ReservationEntityMapper reservationEntityMapper;


    @Override
    public Reservation save(Reservation reservation) {
        JpaReservation save = reservationRepository.save(reservationEntityMapper.toEntity(reservation));
        return reservationEntityMapper.toDomain(save);
    }

    @Override
    public Optional<Reservation> findById(ReservationId reservationId) {
        return reservationRepository.findById(reservationId.value())
                .map(reservationEntityMapper::toDomain);
    }
}
