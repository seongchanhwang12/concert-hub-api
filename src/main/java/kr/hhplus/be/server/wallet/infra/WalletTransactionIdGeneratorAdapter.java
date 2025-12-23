package kr.hhplus.be.server.wallet.infra;

import kr.hhplus.be.server.reservation.app.IdGenerator;
import kr.hhplus.be.server.wallet.domain.WalletTransactionId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
    public class WalletTransactionIdGeneratorAdapter {
    private final IdGenerator idGenerator;

    public  WalletTransactionId generate(){
        return WalletTransactionId.of(idGenerator.nextId());
    }
}
