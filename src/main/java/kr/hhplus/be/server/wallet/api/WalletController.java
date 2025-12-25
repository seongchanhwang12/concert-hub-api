package kr.hhplus.be.server.wallet.api;

import kr.hhplus.be.server.common.app.AuthFacade;
import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.wallet.app.ChargePointUseCase;
import kr.hhplus.be.server.wallet.app.GetPointBalanceUseCase;
import kr.hhplus.be.server.wallet.app.dto.ChargePointCommand;
import kr.hhplus.be.server.wallet.app.dto.ChargePointResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final ChargePointUseCase chargePointUseCase;
    private final GetPointBalanceUseCase pointBalanceUseCase;
    private final AuthFacade authFacade;

    @PostMapping(value = "/me/transactions", params = "type=charge")
    public ResponseEntity<ChargePointResponse> chargePoint(ChargePointRequest request) {
        // TODO userId 는 인증정보에서 가져왔다고 가정
        UserId userId = authFacade.currentUserId();
        ChargePointResult result = chargePointUseCase.chargePoint(new ChargePointCommand(request.idempotencyKey(), userId, request.amount()));
        return ResponseEntity.ok().body(ChargePointResponse.from(result));
    }

    @GetMapping("/me")
    public ResponseEntity<String> getPoints() {
        UserId userId = authFacade.currentUserId();
        pointBalanceUseCase.get(userId);
        return ResponseEntity.ok().body("0");
    }



}
