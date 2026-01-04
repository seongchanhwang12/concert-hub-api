package kr.hhplus.be.server.wallet.domain;

import kr.hhplus.be.server.common.domain.Money;
import kr.hhplus.be.server.common.domain.UserId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PROTECTED)
public class Wallet {

    private final WalletId id;
    private final UserId ownerId;
    private Point balance;

    public static Wallet create(WalletId id, UserId ownerId, Point point) {
        return new Wallet(id, ownerId, point);
    }

    public static Wallet of(WalletId id, UserId ownerId, Point point) {
        return new Wallet(id, ownerId, point);
    }

    public static Wallet empty(WalletId id, UserId ownerId) {
        return new Wallet(id, ownerId, Point.zero());
    }

    public void charge(Point chargePoint){
        balance = balance.plus(chargePoint);
    }

    /**
     * 지갑에서 지불한다.
     * @param paidAmount - 결제 금액
     */
    public Point pay(Money paidAmount) {
        Point paidPoint = Point.of(paidAmount.value());
        return balance = balance.minus(paidPoint);
    }
}
