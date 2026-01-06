package kr.hhplus.be.server.concert.domain.concert;

public record ConcertId(long value) {

    public ConcertId(long value) {
        this.value = value;
    }

    public static ConcertId of(long id) {
        return new ConcertId(id);
    }

    @Override
    public String toString(){
        return String.valueOf(value);
    }


}
