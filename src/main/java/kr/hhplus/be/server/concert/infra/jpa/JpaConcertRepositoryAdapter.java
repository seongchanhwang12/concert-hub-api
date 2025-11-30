package kr.hhplus.be.server.concert.infra.jpa;

import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.domain.ConcertId;
import kr.hhplus.be.server.concert.domain.ConcertRepository;
import kr.hhplus.be.server.concert.infra.ConcertEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaConcertRepositoryAdapter implements ConcertRepository {

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
