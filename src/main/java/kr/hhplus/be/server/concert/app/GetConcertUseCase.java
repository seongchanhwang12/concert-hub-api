package kr.hhplus.be.server.concert.app;

import kr.hhplus.be.server.common.NotFoundException;
import kr.hhplus.be.server.concert.app.exception.ConcertErrorCode;
import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.domain.ConcertId;
import kr.hhplus.be.server.concert.domain.ConcertReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetConcertUseCase {

    private final ConcertReader concertReader;

    /**
     * 콘서트 상세 조회
     * 특정 콘서트 ID로 콘서트 정보를 조회해서 반환합니다.
     *
     * @param concertId
     * @return
     */
    public Concert getConcertDetail(ConcertId concertId) {
        return concertReader.findById(concertId)
                .orElseThrow(()-> new NotFoundException(ConcertErrorCode.NOT_FOUND, concertId.toString()));
    }
}
