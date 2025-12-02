package kr.hhplus.be.server.concert.domain.concert;

import java.util.Optional;

public interface ConcertRepository {

    Optional<Concert> findConcertByConcertId(ConcertId concertId);

    Concert save(Concert concert);



}
