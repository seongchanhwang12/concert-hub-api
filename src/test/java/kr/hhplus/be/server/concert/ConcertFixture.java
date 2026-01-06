package kr.hhplus.be.server.concert;

import kr.hhplus.be.server.concert.domain.concert.Concert;
import kr.hhplus.be.server.concert.domain.concert.ConcertId;

import java.time.LocalDate;

public class ConcertFixture {

    /** Fixtures */
    public static Concert createConcert(){
        LocalDate startAt = LocalDate.now();
        LocalDate endAt = LocalDate.now().plusDays(1);
        String title = "black pink world tour concert ";
        String description = "description for " + title;

        return Concert.of(title, startAt, endAt, description);
    }

    public static Concert createConcert(ConcertId id){
        LocalDate startAt = LocalDate.now();
        LocalDate endAt = LocalDate.now().plusDays(1);
        String title = "black pink world tour concert " + id.value();
        String description = "description for " + title;

        return Concert.of(title, startAt, endAt, description);
    }


}
