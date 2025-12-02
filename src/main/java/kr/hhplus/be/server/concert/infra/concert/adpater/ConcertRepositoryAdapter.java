package kr.hhplus.be.server.concert.infra.concert.adpater;

import kr.hhplus.be.server.concert.domain.concert.Concert;
import kr.hhplus.be.server.concert.domain.concert.ConcertId;
import kr.hhplus.be.server.concert.domain.concert.ConcertRepository;
import kr.hhplus.be.server.concert.infra.concert.jpa.JpaConcert;
import kr.hhplus.be.server.concert.infra.concert.jpa.JpaConcertRepository;
import kr.hhplus.be.server.concert.infra.concert.mapping.ConcertEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryAdapter implements ConcertRepository {

    private final JpaConcertRepository jpa;
    private final ConcertEntityMapper mapper;

    @Override
    public Optional<Concert> findConcertByConcertId(ConcertId concertId) {
        return jpa.findById(concertId.value())
                .map(mapper::toDomain);
    }

    @Override
    public Concert save(Concert concert) {
        JpaConcert save = jpa.save(mapper.toEntity(concert));
        return mapper.toDomain(save);

    }
}
