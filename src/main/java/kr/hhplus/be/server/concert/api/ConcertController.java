package kr.hhplus.be.server.concert.api;

import jakarta.validation.constraints.NotNull;
import kr.hhplus.be.server.concert.app.GetConcertUseCase;
import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.domain.ConcertDetail;
import kr.hhplus.be.server.concert.domain.ConcertId;
import kr.hhplus.be.server.schedule.domain.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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

        List<ScheduleSummary> summaries = concertDetail.schedules().stream()
                .map(ScheduleSummary::from)
                .toList();

        return ResponseEntity.ok(GetConcertResponse.from(concertDetail.concert(), summaries));
    }



}
