package kr.hhplus.be.server.concert.app;

import kr.hhplus.be.server.common.NotFoundException;
import kr.hhplus.be.server.concert.app.domain.FakeIdGenerator;
import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.domain.ConcertId;
import kr.hhplus.be.server.concert.domain.ConcertReader;
import kr.hhplus.be.server.concert.domain.IdGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GetConcertUseCaseTest {

    @InjectMocks
    GetConcertUseCase getConcertUseCase;

    @Mock
    ConcertReader concertRepository;

    IdGenerator idGenerator = new FakeIdGenerator();

    /**
     * 검증 :
     *  - 올바른 ConcertId로 조회시, Concert가 null이면 안된다.
     *  - ConcertId와 조회된 Concert의 id 값이 같아야 한다.
     */
    @DisplayName("올바른 concertId 를 받으면, 콘서트 정보를 반환한다.")
    @Test
    void getConcert_success() {
        //given
        UUID uuid = idGenerator.nextId();
        ConcertId concertId = ConcertId.of(uuid);
        String title = "concert title";
        String description = "concert description";
        LocalDate startAt = LocalDate.now();
        String endAt = LocalDate.now().plusDays(1).toString();
        Concert concert = Concert.of(concertId, title, startAt, LocalDate.parse(endAt), description);

        given(concertRepository.findById(concertId))
                .willReturn(Optional.of(concert));

        //when
        Concert actual = getConcertUseCase.getConcertDetail(concertId);

        //then
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(concertId);

    }

    /**
     * 검증 :
     *  - ConcertId로 값이 조회되지 않는 경우 NotFoundException을 throw 한다.
     */
    @DisplayName("concertId가 올바르지 않으면, NotFoundException 을 반환한다")
    @Test
    void should_throw_exception_when_invalid_concert_id() {
        //given
        UUID uuid = idGenerator.nextId();
        ConcertId concertId = ConcertId.of(uuid);

        //when & then
        assertThatThrownBy(() -> getConcertUseCase.getConcertDetail(concertId))
                .isInstanceOf(NotFoundException.class);

    }





}