package kr.hhplus.be.server.concert.infra.seat.jpa;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaSeatRepository extends JpaRepository<JpaSeat, Long> {

    List<JpaSeat> findSeatsByScheduleId(Long scheduleId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from JpaSeat s where s.id = :id")
    Optional<JpaSeat> findByIdForUpdate(@Param("id") long value);
}
