package kr.hhplus.be.server.concert.api;

import kr.hhplus.be.server.concert.app.GetConcertUseCase;
import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.domain.ConcertId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public ResponseEntity<GetConcertResponse> getConcert(@PathVariable UUID concertId) {
        Concert concertDetail = getConcertUseCase.getConcertDetail(ConcertId.of(concertId));
        return ResponseEntity.ok(GetConcertResponse.from(concertDetail));
    }



}
