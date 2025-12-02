package kr.hhplus.be.server.concert.api.concert;

import kr.hhplus.be.server.concert.api.schedule.ScheduleResponse;
import kr.hhplus.be.server.concert.app.concert.exception.GetConcertUseCase;
import kr.hhplus.be.server.concert.app.seat.ListSeatsUseCase;
import kr.hhplus.be.server.concert.domain.concert.ConcertDetail;
import kr.hhplus.be.server.concert.domain.concert.ConcertId;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/concerts")
public class ConcertController {

    private final GetConcertUseCase getConcertUseCase;

    /**
     * Concert 상세 정보 조회
     *
     * @param concertId
     * @return
     */
    @GetMapping("/{concertId}")
    public ResponseEntity<GetConcertResponse> getConcert(@PathVariable Long concertId) {
        ConcertDetail concertDetail = getConcertUseCase.getConcertDetail(ConcertId.of(concertId));

        List<ScheduleResponse> summaries = concertDetail.schedules()
                .stream()
                .map(ScheduleResponse::from)
                .toList();

        return ResponseEntity.ok(GetConcertResponse.from(concertDetail.concert(), summaries));
    }

}
