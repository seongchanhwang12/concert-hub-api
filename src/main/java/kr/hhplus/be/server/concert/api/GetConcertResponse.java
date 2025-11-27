package kr.hhplus.be.server.concert.api;

import kr.hhplus.be.server.concert.domain.Concert;

public record GetConcertResponse(String concertId, String title, String startAt, String endAt, String description) {


    public static GetConcertResponse from(Concert concertDetail) {
        return new GetConcertResponse(
                concertDetail.getId().toString(),
                concertDetail.getTitle(),
                concertDetail.getStartAt().toString(),
                concertDetail.getEndAt().toString(),
                concertDetail.getDescription());
    }
}
