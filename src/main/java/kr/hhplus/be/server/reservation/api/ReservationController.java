package kr.hhplus.be.server.reservation.api;

import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.concert.domain.seat.SeatId;
import kr.hhplus.be.server.reservation.app.ReserveSeatCommand;
import kr.hhplus.be.server.reservation.app.ReserveSeatUseCase;
import kr.hhplus.be.server.reservation.domain.ReservationSeat;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservation")
public class ReservationController {

    private final ReserveSeatUseCase reserveSeatUseCase;

    @PostMapping
    public ResponseEntity<ReservationResponse> reserve(@RequestBody ReservationRequest request) {
        // TODO 추후 JWT 토큰 구현시 교체
        final UserId userId = new UserId(UUID.randomUUID());
        final SeatId seatId = SeatId.of(request.seatId());

        ReservationSeat reservationSeat = reserveSeatUseCase.reserveSeat(ReserveSeatCommand.of(userId,seatId));
        return ResponseEntity.ok().body(ReservationResponse.of(reservationSeat));
    }

}
