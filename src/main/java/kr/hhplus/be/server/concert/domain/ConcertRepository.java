package kr.hhplus.be.server.concert.domain;

import java.util.Optional;

public interface ConcertRepository {

    Optional<Concert> findConcertByConcertId(ConcertId concertId);

    Concert save(Concert concert);



}
