package kr.hhplus.be.server.concert.domain;

import java.util.UUID;

public record ConcertId(UUID value) {

    public ConcertId(UUID value) {
        this.value = value;
    }

    public static ConcertId of(UUID concertId) {
        return new ConcertId(concertId);
    }

    @Override
    public String toString(){
        return value.toString();
    }


}
