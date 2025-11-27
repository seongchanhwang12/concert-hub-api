package kr.hhplus.be.server.concert.infra;

import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.domain.ConcertId;
import kr.hhplus.be.server.concert.infra.jpa.JpaConcert;
import kr.hhplus.be.server.config.mapstruct.MapstructMapperConfig;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(config = MapstructMapperConfig.class)
public interface ConcertEntityMapper {

    Concert toDomain(JpaConcert src);

    JpaConcert toEntity(Concert concert);

    /**
     * UUID 와 ConcertId 매핑
     *
     * @param concertId
     * @return
     */
    default ConcertId toConcertId(UUID concertId) {
        return new ConcertId(concertId);
    }

    /**
     *
     * @param concertId
     * @return
     */
    default UUID toUuid(ConcertId concertId) {
        return concertId.value();
    }



}
