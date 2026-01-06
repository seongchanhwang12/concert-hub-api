package kr.hhplus.be.server.concert.infra.concert.mapping;

import kr.hhplus.be.server.concert.domain.concert.Concert;
import kr.hhplus.be.server.concert.domain.concert.ConcertId;
import kr.hhplus.be.server.concert.infra.concert.jpa.JpaConcert;
import kr.hhplus.be.server.config.mapstruct.MapstructMapperConfig;
import org.mapstruct.Mapper;

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
    default ConcertId toConcertId(Long concertId) {
        return ConcertId.of(concertId);
    }

    /**
     * Concert 객체를 JpaConcert 매핑할 때 ConcertId 를 Long 으로 변환
     * Concert 신규 생성시 GeneratedValue 전략으로 ID 가 생성되므로 ConcertId 가 null 일 수 있음
     *
     * @param concertId
     * @return
     */
    default Long toEntityId (ConcertId concertId) {
        return concertId == null ? null : concertId.value();
    }



}
