package kr.hhplus.be.server.queue.app;

import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.concert.app.domain.FakeIdGenerator;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.payment.domain.QueueTokenRepository;
import kr.hhplus.be.server.queue.domain.QueueToken;
import kr.hhplus.be.server.queue.domain.QueueTokenStatus;
import kr.hhplus.be.server.reservation.app.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class QueueServiceTest {

    @InjectMocks
    QueueService queueService;

    @Mock
    QueueTokenRepository queueTokenRepository;

    @Captor
    ArgumentCaptor<QueueToken> queueTokenCaptor;

    Clock clock;

    IdGenerator idGenerator = new FakeIdGenerator();

    @BeforeEach
    void setUp(){
        Instant parse = Instant.parse("2025-01-01T10:00:00Z");
        clock = Clock.fixed(parse, ZoneId.of("UTC"));

        queueService = new QueueService(queueTokenRepository, clock);
    }


    /**
     * 특정 사용자에게 이미 발행된 토큰 반환 테스트
     * - 이미 발급된 토큰이 있는 경우 조회 후 반환
     */
    @Test
    @DisplayName("특정 사용자에게 발행된 토큰이 있고, 만료 상태가 아니면, 토큰을 반환한다.")
    void given_userId_And_ScheduleId_When_issueQueueToken_Then_return_token() {
        //given
        long scheduleIdValue = 1L;
        UUID userIdValue = UUID.randomUUID();

        UserId userId = UserId.of(userIdValue);
        ScheduleId scheduleId = ScheduleId.of(scheduleIdValue);

        QueueToken queueToken = QueueToken.builder()
                .userId(userId)
                .scheduleId(scheduleId)
                .status(QueueTokenStatus.ACTIVE)
                .tokenValue(idGenerator.nextId())
                .build();

        IssueQueueTokenCommand cmd = new IssueQueueTokenCommand(scheduleIdValue, userId);
        given(queueTokenRepository.findByUserIdAndScheduleId(userId,scheduleId)).willReturn(Optional.of(queueToken));

        //when
        IssueQueueTokenResult actual = queueService.issueQueueToken(cmd);

        //then
        then(queueTokenRepository).should().findByUserIdAndScheduleId(userId, scheduleId);
        then(queueTokenRepository).should(never()).save(any(QueueToken.class));

        assertThat(actual).isNotNull();
        assertThat(actual.scheduleId()).isEqualTo(scheduleId);
        assertThat(actual.status()).isEqualTo(QueueTokenStatus.ACTIVE);
        assertThat(actual.tokenValue()).isEqualTo(queueToken.getTokenValue());
    }

    /**
     * 발행되어 있는 토큰이 만료일경우 재발행 테스트
     * 발행된 토큰이 만료상태일경우 다음을 검증
     * - 재발행시 재발행 토큰의 상태가 레파지토리에 저장되어야 한다.
     * - 재발행 토큰 상태는 ACTIVE 여야한다.
     * - 재발행 토큰의 tokenValue 가 기존 tokenValue 와 달라야 한다.
     * - 재발행 토큰의 발행일시가 기존 발행일시와 달라야한다.
     */
    @Test
    @DisplayName("특정 사용자에게 발행된 토큰이 있고, 만료 상태면, 토큰을 재발행한다.")
    void given_scheduleId_When_issueQueueToken_Then_return_token() {
        //given
        long scheduleIdValue = 1L;
        UUID userIdValue = UUID.randomUUID();

        UserId userId = UserId.of(userIdValue);
        ScheduleId scheduleId = ScheduleId.of(scheduleIdValue);
        UUID originTokenValue = idGenerator.nextId();

        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime originalIssuedAt = now.minusMinutes(10);

        QueueToken queueToken = QueueToken.builder()
                .userId(userId)
                .scheduleId(scheduleId)
                .status(QueueTokenStatus.EXPIRED)
                .tokenValue(originTokenValue)
                .issuedAt(originalIssuedAt)
                .build();

        IssueQueueTokenCommand cmd = new IssueQueueTokenCommand(scheduleIdValue, userId);

        given(queueTokenRepository.findByUserIdAndScheduleId(userId,scheduleId))
                .willReturn(Optional.of(queueToken));

        given(queueTokenRepository.save(any(QueueToken.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        //when
        queueService.issueQueueToken(cmd);

        //then
        then(queueTokenRepository).should().save(queueTokenCaptor.capture());
        QueueToken queueTokenCaptorValue = queueTokenCaptor.getValue();
        assertThat(queueTokenCaptorValue).isNotNull();
        assertThat(queueTokenCaptorValue.getStatus()).isEqualTo(QueueTokenStatus.ACTIVE);
        assertThat(queueTokenCaptorValue.getTokenValue()).isNotEqualTo(originTokenValue);
        assertThat(queueTokenCaptorValue.getIssuedAt()).isEqualTo(now);

    }

    /**
     * 대기열 토큰 최초 발행시 테스트
     * - userId 와 scheduleId 로 발행된 토큰이 있는지 조회해야 한다.
     * - 발행된 토큰이 없읅
     * - 저장된 토큰의 데이터를 발행 결과로 반환한다.
     * - 최초 발행시 saveAndFlush 를 통해 자장후 즉시 플러시 되어야한다. (멱등성 대응)
     */
    @Test
    @DisplayName("특정 사용자에게 발행된 토큰이 없으면, 토큰을 저장하고, 저장한 토큰을 반환한다")
    void given_scheduleId_When_not_exists_issueQueueToken_Then_save_And_return_token() {
        //given
        long scheduleIdValue = 1L;
        UUID userIdValue = UUID.randomUUID();

        UserId userId = UserId.of(userIdValue);
        ScheduleId scheduleId = ScheduleId.of(scheduleIdValue);
        IssueQueueTokenCommand cmd = new IssueQueueTokenCommand(scheduleIdValue, userId);

        given(queueTokenRepository.findByUserIdAndScheduleId(userId,scheduleId))
                .willReturn(Optional.empty());

        given(queueTokenRepository.saveAndFlush(any(QueueToken.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        //when
        IssueQueueTokenResult actual = queueService.issueQueueToken(cmd);

        //then
        then(queueTokenRepository).should().findByUserIdAndScheduleId(eq(userId), eq(scheduleId));

        // 토큰 검사
        then(queueTokenRepository).should().saveAndFlush(queueTokenCaptor.capture());
        QueueToken queueTokenCaptorValue = queueTokenCaptor.getValue();
        assertThat(queueTokenCaptorValue).isNotNull();
        assertThat(queueTokenCaptorValue.getStatus()).isEqualTo(QueueTokenStatus.ACTIVE);
        assertThat(queueTokenCaptorValue.getUserId()).isEqualTo(userId);
        assertThat(queueTokenCaptorValue.getScheduleId()).isEqualTo(scheduleId);
        assertThat(queueTokenCaptorValue.getTokenValue()).isNotNull();

        // 반환 결과가 발행된 토큰의 값과 같은지 검사
        assertThat(queueTokenCaptorValue.getTokenValue()).isEqualTo(actual.tokenValue());
        assertThat(queueTokenCaptorValue.getUserId()).isEqualTo(actual.userId());
        assertThat(queueTokenCaptorValue.getScheduleId()).isEqualTo(actual.scheduleId());

        // save 함수는 재발행시 재발행 토큰의 업데이트에시에만 실행되어야 한다.
        then(queueTokenRepository).should(never()).save(any(QueueToken.class));

    }

    /**
     * 동시성 예외시 멱등성 테스트
     * 동시성 (네트워크 지연 또는 중복 요청) 문제 발생시 DuplicateQueueTokenException 발생 상황
     * - userId와 scheduleId로 발급된 토큰 조회해야한다.
     * - 조회된 토큰이 만료 (EXPIRED) 상태면 토큰 재발행 후 상태를 저장한다.
     */
    @Test
    @DisplayName("특정 사용자에게 발행된 토큰이 없고, 토큰저장시 unique 예외 발생시, 토큰을 재발행하여 반환한다.")
    void given_scheduleId_When_save_and_throw_duplicateException_Then_find_And_reissue_token() {
        //given
        long scheduleIdValue = 1L;
        UUID userIdValue = UUID.randomUUID();

        UserId userId = UserId.of(userIdValue);
        ScheduleId scheduleId = ScheduleId.of(scheduleIdValue);
        IssueQueueTokenCommand cmd = new IssueQueueTokenCommand(scheduleIdValue, userId);

        given(queueTokenRepository.findByUserIdAndScheduleId(userId,scheduleId))
                .willReturn(Optional.empty());

        given(queueTokenRepository.saveAndFlush(any(QueueToken.class)))
                .willThrow(new DuplicateQueueTokenException("duplicate",null));

        QueueToken existingExpired = QueueToken.builder()
                .userId(userId)
                .scheduleId(scheduleId)
                .status(QueueTokenStatus.EXPIRED)
                .tokenValue(UUID.randomUUID())
                .issuedAt(LocalDateTime.now(clock).minusMinutes(10))
                .build();

        given(queueTokenRepository.findByUserIdAndScheduleId(userId,scheduleId))
                .willAnswer(invocation -> Optional.empty())
                .willAnswer(invocation -> Optional.of(existingExpired));

        given(queueTokenRepository.save(any(QueueToken.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        //when
        queueService.issueQueueToken(cmd);

        //then
        then(queueTokenRepository).should().saveAndFlush(any(QueueToken.class));

        then(queueTokenRepository).should(times(2)).findByUserIdAndScheduleId(eq(userId), eq(scheduleId));

        then(queueTokenRepository).should().save(queueTokenCaptor.capture());

        QueueToken queueTokenCaptorValue = queueTokenCaptor.getValue();
        assertThat(queueTokenCaptorValue).isNotNull();
    }

    /**
     * 대기 상태 토큰 발행 테스트
     * - userId 와 scheduleId 로 발행된 토큰이 있는지 조회해야 한다.
     * - 발행된 토큰이 없읅
     * - 저장된 토큰의 데이터를 발행 결과로 반환한다.
     * - 최초 발행시 saveAndFlush 를 통해 자장후 즉시 플러시 되어야한다. (멱등성 대응)
     */
    @Test
    @DisplayName("특정 사용자에게 발행된 토큰이 없고, 최대 활성 수량을 넘은 경우 대기 상태의 토큰을 저장하고, 결과를 반환한다")
    void given_scheduleId_When_not_exists_issueQueueToken_Then_save_And_return_waiting_token() {
        //given
        long scheduleIdValue = 1L;
        UUID userIdValue = UUID.randomUUID();

        UserId userId = UserId.of(userIdValue);
        ScheduleId scheduleId = ScheduleId.of(scheduleIdValue);
        IssueQueueTokenCommand cmd = new IssueQueueTokenCommand(scheduleIdValue, userId);

        given(queueTokenRepository.findByUserIdAndScheduleId(userId,scheduleId))
                .willReturn(Optional.empty());

        given(queueTokenRepository.countActiveTokens(scheduleId)).willReturn(51L);

        given(queueTokenRepository.saveAndFlush(any(QueueToken.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        //when
        queueService.issueQueueToken(cmd);

        //then
        then(queueTokenRepository).should().findByUserIdAndScheduleId(eq(userId), eq(scheduleId));

        // 토큰 검사
        then(queueTokenRepository).should().saveAndFlush(queueTokenCaptor.capture());
        QueueToken queueTokenCaptorValue = queueTokenCaptor.getValue();
        assertThat(queueTokenCaptorValue).isNotNull();
        assertThat(queueTokenCaptorValue.getStatus()).isEqualTo(QueueTokenStatus.WAITING);

    }







}