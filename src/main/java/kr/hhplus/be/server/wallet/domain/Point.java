package kr.hhplus.be.server.wallet.domain;

public record Point(long balance) {

    public static Point of(long balance) {
        return new Point(balance);
    }

    public static Point zero(){
        return new Point(0);
    }

    public Point plus(Point chargePoint) {
        return new Point(balance + chargePoint.balance);
    }
}
