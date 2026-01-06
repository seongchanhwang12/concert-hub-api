package kr.hhplus.be.server.reservation.app;

import kr.hhplus.be.server.reservation.domain.ReservationId;

import java.util.UUID;

public interface IdGenerator{
     UUID nextId();
}
