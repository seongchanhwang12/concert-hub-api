package kr.hhplus.be.server.concert.infra.seat.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaSeatRepository extends JpaRepository<JpaSeat, Long> {

    List<JpaSeat> findSeatsByScheduleId(Long scheduleId);
}
