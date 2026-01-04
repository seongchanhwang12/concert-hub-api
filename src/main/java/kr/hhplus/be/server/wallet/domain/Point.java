package kr.hhplus.be.server.wallet.domain;

public record Point(long balance) {

    /**
     * 포인트 음수 방지
     * @param balance - 잔액
     */
    public Point{
        if(balance < 0){
            throw new IllegalArgumentException("Point balance cannot be negative");
        }
    }

    public static Point of(long balance) {
        return new Point(balance);
    }

    public static Point zero(){
        return new Point(0);
    }

    public Point plus(Point chargePoint) {
        return new Point(balance + chargePoint.balance);
    }

    public Point minus(Point point) {
        return new Point(balance - point.balance);
    }
}
