package kr.hhplus.be.server.common.domain;

public record Money(long value) {

    public static Money wons(long i) {
        return new Money(i);
    }
}
