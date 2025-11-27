package kr.hhplus.be.server.concert.domain;

import java.util.Optional;

public interface ConcertReader {
    Optional<Concert> findById(ConcertId concertId);

}
