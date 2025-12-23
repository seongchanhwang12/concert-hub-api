package kr.hhplus.be.server.wallet.app;

import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.concert.app.domain.FakeIdGenerator;
import kr.hhplus.be.server.reservation.app.IdGenerator;
import kr.hhplus.be.server.wallet.app.dto.ChargePointCommand;
import kr.hhplus.be.server.wallet.app.dto.ChargePointResult;
import kr.hhplus.be.server.wallet.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ChargePointUseCaseTest {

    ChargePointUseCase chargePointUseCase;

    @Mock
    WalletRepository walletRepository;

    @Mock
    WalletTransactionRepository walletTransactionRepository;

    IdGenerator idGenerator = new FakeIdGenerator();

    @BeforeEach
    void setUp(){
        WalletTransactionFactory walletTransactionFactory = new WalletTransactionFactory(new FakeIdGenerator());
        WalletFactory walletFactory = new WalletFactory(new FakeIdGenerator());
        chargePointUseCase = new ChargePointUseCase(walletRepository, walletTransactionRepository, walletTransactionFactory,walletFactory);
    }

    @Nested
    @DisplayName("이미 처리된 충전 요청일 때,")
    class WhenIdempotencyKeyAlreadyExists{
        @Test
        @DisplayName("요청 값이 달라도 idempotencyKey가 같으면 기존 거래를 반환하고, wallet/tx 저장은 수행하지 않는다.")
        void givenDifferentAmountButSameIdempotencyKey_whenChargePoint_thenReturnExistingTransactionAndDoNotMutateState() {
            // given
            UUID userIdValue = UUID.randomUUID();
            UserId userId = UserId.of(userIdValue);

            UUID idempotencyKey = idGenerator.nextId();

            Point existingChargedAmount = Point.of(10_000L);
            Point existingBalanceAfter = Point.of(20_000L);

            // 요청은 "다른 금액"이지만, idempotencyKey가 같음
            ChargePointCommand cmd = new ChargePointCommand(idempotencyKey, userIdValue, 999_999L);

            // 멱등 선점 실패 = 이미 처리된 키(중복 요청)
            given(walletTransactionRepository.trySaveIdempotency(any(WalletTransaction.class)))
                    .willReturn(false);

            // when
            ChargePointResult actual = chargePointUseCase.chargePoint(cmd);

            // then
            assertThat(actual.chargedPoint()).isEqualTo(existingChargedAmount);
            assertThat(actual.balanceAfter()).isEqualTo(existingBalanceAfter);

            // 중복 요청이면 상태 변경이 없어야 함
            then(walletRepository).shouldHaveNoInteractions();
            then(walletTransactionRepository).should(never()).save(any());
        }

    }

    @Captor ArgumentCaptor<WalletTransaction> walletTransactionCaptor;
    @Captor ArgumentCaptor<Wallet> walletCaptor;

    @Nested
    @DisplayName("지갑이 생성 되어있을 때,")
    class WhenIdempotencyKeyNotExists{
        UUID idempotencyKey;
        UserId userId;
        Point pointAmount;
        Wallet wallet;

        @BeforeEach
        void setUp(){
            idempotencyKey = idGenerator.nextId();
            userId =  UserId.of(UUID.randomUUID());
            pointAmount = Point.of(10_000L);

            // 사용자 지갑이 회원가입시 정상 생성 되었다고 가정
            wallet = WalletFixture.createWith(userId, Point.zero());
            given(walletRepository.findByOwnerId(eq(userId)))
                    .willReturn(Optional.of(wallet));
        }

        @Test
        @DisplayName("충전이 성공하면, 지갑이 충전 금액 만큼 충전되고, 충전 결과를 반환한다.")
        void Given_wallet_When_charge_wallet_Then_create_charge_tx_and_return(){
            // given
            ChargePointCommand cmd = new ChargePointCommand(idempotencyKey,userId.value(),pointAmount.balance() );

            given(walletTransactionRepository.trySaveIdempotency(any(WalletTransaction.class)))
                    .willReturn(true);

            // when
            ChargePointResult actual = chargePointUseCase.chargePoint(cmd);

            // then
            then(walletTransactionRepository).should().trySaveIdempotency(walletTransactionCaptor.capture());
            WalletTransaction capturedChargeTx = walletTransactionCaptor.getValue();
            assertThat(capturedChargeTx.getId()).isEqualTo(actual.transactionId());
            assertThat(capturedChargeTx.getBalanceAfter()).isEqualTo(actual.balanceAfter());
            assertThat(capturedChargeTx.getPointAmount()).isEqualTo(actual.chargedPoint());

            then(walletRepository).should().save(walletCaptor.capture());
            Wallet capturedWallet = walletCaptor.getValue();
            assertThat(capturedWallet.getPoint()).isEqualTo(pointAmount);
            assertThat(capturedWallet.getPoint()).isEqualTo(actual.chargedPoint());
        }

        @Test
        @DisplayName("이미 동일한 충전 요청이 처리된 경우, 충전이 실패하고, 해당 충전 내역을 조회해 충전 결과를 반환한다.")
        void Given_already_charged_when_charge_point_then_find_chargedTx_and_return(){
            // given
            ChargePointCommand cmd = new ChargePointCommand(idempotencyKey,userId.value(),pointAmount.balance() );


            WalletTransaction charge = WalletTransactionFixture.createCharge(userId, pointAmount, Point.zero().plus(pointAmount), idempotencyKey);

            given(walletTransactionRepository.trySaveIdempotency(any(WalletTransaction.class)))
                    .willReturn(false);

            given(walletTransactionRepository.findByOwnerIdAndIdempotencyKey(eq(userId), eq(idempotencyKey)))
                    .willReturn(Optional.of(charge));


            // when
            ChargePointResult actual = chargePointUseCase.chargePoint(cmd);

            // then
            then(walletTransactionRepository).should().findByOwnerIdAndIdempotencyKey(userId, idempotencyKey);
            then(walletRepository).should(never()).save(any(Wallet.class));
            assertThat(actual.chargedPoint()).isEqualTo(pointAmount);


        }


        @Test
        void When_wallet_not_exists_Then_save_wallet() {
            //given
            ChargePointCommand cmd = new ChargePointCommand(idempotencyKey,userId.value(),pointAmount.balance());
            given(walletRepository.findByOwnerId(userId))
                    .willReturn(Optional.empty());

            //when
            chargePointUseCase.chargePoint(cmd);

            //then
            then(walletRepository).should().save(walletCaptor.capture());
            Wallet wallet = walletCaptor.getValue();
            assertThat(wallet.getPoint()).isEqualTo(pointAmount);
            assertThat(wallet.getOwnerId()).isEqualTo(userId);
        }

    }

}