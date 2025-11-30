package kr.hhplus.be.server.concert.app;

import kr.hhplus.be.server.common.NotFoundException;
import kr.hhplus.be.server.concert.app.exception.ConcertErrorCode;
import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.domain.ConcertDetail;
import kr.hhplus.be.server.concert.domain.ConcertId;
import kr.hhplus.be.server.concert.domain.ConcertRepository;
import kr.hhplus.be.server.schedule.domain.Schedule;
import kr.hhplus.be.server.schedule.domain.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
                .orElseThrow(() -> new NotFoundException(ConcertErrorCode.NOT_FOUND, concertId.toString()));

        List<Schedule> schedules = scheduleRepository.findSchedulesByConcertId(concertId);

        return ConcertDetail.from(concert, schedules);
    }
}
