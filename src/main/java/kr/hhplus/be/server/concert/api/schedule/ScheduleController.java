package kr.hhplus.be.server.concert.api.schedule;

import kr.hhplus.be.server.concert.app.schedule.GetScheduleUseCase;
import kr.hhplus.be.server.concert.app.seat.ListSeatsUseCase;
import kr.hhplus.be.server.concert.domain.schedule.Schedule;
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
    private final GetScheduleUseCase getScheduleUseCase;

    /**
     * 스케줄별 좌석 조회
     *
     * @param scheduleId - 스케줄 고유 번호
     * @return ResponseEntity<ScheduleResponse> - 스케줄 상세 응답
     */
    @GetMapping("/{scheduleId}/seats")
    public ResponseEntity<SeatsResponse> getSeats(@PathVariable Long scheduleId) {
        Seats seats = listSeatsUseCase.listSeats(ScheduleId.of(scheduleId));
        return ResponseEntity.ok(SeatsResponse.from(seats));
    }

    /**
     * 스케줄 상세 조회
     * @param scheduleId - 스케줄 고유 번호
     * @return ResponseEntity<ScheduleResponse>
     */
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponse> getSchedule(@PathVariable Long scheduleId) {
        Schedule scheduleDetail = getScheduleUseCase.getScheduleDetail(ScheduleId.of(scheduleId));
        return ResponseEntity.ok(ScheduleResponse.from(scheduleDetail));
    }
}
