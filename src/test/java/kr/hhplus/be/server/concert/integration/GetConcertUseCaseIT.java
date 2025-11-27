package kr.hhplus.be.server.concert.integration;

import kr.hhplus.be.server.PostgresTestcontainersConfig;
import kr.hhplus.be.server.concert.app.GetConcertUseCase;
import kr.hhplus.be.server.concert.app.domain.FakeIdGenerator;
import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.domain.ConcertId;
import kr.hhplus.be.server.concert.domain.ConcertWriter;
import kr.hhplus.be.server.concert.domain.IdGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({PostgresTestcontainersConfig.class})
class GetConcertUseCaseIT {

    IdGenerator idGenerator = new FakeIdGenerator();

    @Autowired
    GetConcertUseCase getConcertUseCase;

    @Autowired
    ConcertWriter writer;

    @DisplayName("유효한 ID로 조회하면, 콘서트를 반환한다.")
    @Test
    void use_case_is_not_null() {
        //given
        ConcertId concertId = ConcertId.of(idGenerator.nextId());
        String title = "concert title";
        String description = "concert description";
        LocalDate startAt = LocalDate.now();
        String endAt = LocalDate.now().plusDays(1).toString();

        Concert concert = Concert.of(concertId, title, startAt, LocalDate.parse(endAt), description);
        Concert savedConcert = writer.save(concert);

        //when
        Concert actual = getConcertUseCase.getConcertDetail(concertId);

        //then
        assertThat(actual.getId()).isEqualTo(savedConcert.getId());


    }
}