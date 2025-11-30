package kr.hhplus.be.server.concert.infra.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaConcertRepository extends JpaRepository<JpaConcert, Long> {
}
