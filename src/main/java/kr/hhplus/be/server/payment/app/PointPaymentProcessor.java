package kr.hhplus.be.server.payment.app;

import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.payment.domain.Payment;
import kr.hhplus.be.server.payment.domain.PaymentType;
import kr.hhplus.be.server.wallet.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PointPaymentProcessor implements PaymentProcessor {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final WalletTransactionFactory transactionFactory;

    /**
     * 포인트 결제 처리
     */
    public PaymentProcessResult pay(Payment payment, UUID idempotencyKey) {
        final UserId userId = payment.getUserId();

        // 사용자 포인트 조회
        Wallet wallet = walletRepository.findByOwnerId(userId).orElseThrow();

        // 포인트 차감
        wallet.pay(payment.getPaidAmount());

        // 포인트 사용 트랜잭션 생성
        WalletTransaction useTransaction = transactionFactory.createUse(wallet, idempotencyKey, payment.getId());

        // 멱등 저장 시도
        boolean saveSuccess = walletTransactionRepository.trySaveIdempotency(useTransaction);
        if(saveSuccess){
            walletRepository.save(wallet);
            // return PaymentProcessResult.of(payment.getId(), payment.getPaidAmount(), PaymentType.POINT);
            return  null;
        }

        WalletTransaction walletTransaction = walletTransactionRepository.findByOwnerIdAndIdempotencyKey(userId, idempotencyKey)
                .orElseThrow();

        return PaymentProcessResult.from(payment);
    }
}
