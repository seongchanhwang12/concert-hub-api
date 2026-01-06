package kr.hhplus.be.server.concert.app.concert.exception;

import kr.hhplus.be.server.common.domain.exception.ApplicationException;
import kr.hhplus.be.server.concert.domain.concert.Concert;
import kr.hhplus.be.server.concert.domain.concert.ConcertDetail;
import kr.hhplus.be.server.concert.domain.concert.ConcertId;
import kr.hhplus.be.server.concert.domain.concert.ConcertRepository;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleRepository;
import kr.hhplus.be.server.concert.domain.schedule.Schedules;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetConcertUseCase {

    private final ConcertRepository concertRepository;
    private final ScheduleRepository scheduleRepository;

    /**
     * 콘서트 상세 조회
     * 특정 콘서트 ID로 콘서트 정보를 조회해서 반환합니다.
     *
     * @param concertId
     * @return
     */
    @Transactional(readOnly = true)
    public ConcertDetail getConcertDetail(ConcertId concertId) {
        Concert concert = concertRepository.findConcertByConcertId(concertId)
                .orElseThrow(() -> new ApplicationException(ConcertErrorCode.NOT_FOUND, concertId.toString()));

        Schedules schedules = scheduleRepository.findSchedulesByConcertId(concertId);

        return ConcertDetail.from(concert, schedules);
    }
}
