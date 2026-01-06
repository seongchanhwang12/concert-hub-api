package kr.hhplus.be.server.common.api;

public record ErrorResponse(int status, String errorCode, String errorMsg) {

}
