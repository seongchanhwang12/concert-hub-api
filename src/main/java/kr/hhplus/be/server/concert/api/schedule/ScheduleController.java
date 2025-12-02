package kr.hhplus.be.server.concert.api.schedule;

import kr.hhplus.be.server.concert.app.seat.ListSeatsUseCase;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.concert.domain.seat.Seats;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedules")
public class ScheduleController {
    private final ListSeatsUseCase listSeatsUseCase;

    /**
     * 스케줄별 좌석 조회
     *
     * @param scheduleId
     * @return
     */
    @GetMapping("/{scheduleId}/seats")
    public ResponseEntity<SeatsResponse> getSeats(@PathVariable Long scheduleId) {
        Seats seats = listSeatsUseCase.listSeats(ScheduleId.of(scheduleId));
        return ResponseEntity.ok(SeatsResponse.from(seats));
    }
}
