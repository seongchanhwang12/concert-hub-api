## 프로젝트
# 콘서트 예약 서비스

## Getting Started
> 대기열 + 좌석 임시배정 + 포인트 충전식 결제 기반의 콘서트 예약 서비스.

### Prerequisites
## 문서
- [API 명세서](./docs/openapi.yaml)
- [API spec](./docs/api-spec.md)
- [ERD](./docs/erd.md)
- [인프라 구성도](./docs/infra.md)
- [시퀀스 다이어그램](./docs/sequence-diagram.md)

## 목표 시나리오 (선정)
- **대기열 기반 콘서트 예약**
    1) 사용자는 로그인 후 대기열에 진입해 토큰을 발급받는다.
    2) 활성(Active) 상태의 사용자만 좌석 조회/예약/결제 가능.
    3) 좌석 예약 시 **임시배정(ex> 5분 TTL)** 이 설정되어 타 사용자가 접근 불가.
    4) 임시배정 내 결제가 완료되면 확정, 아니면 만료되어 재판매 가능.
    5) 결제 수단은 **포인트 충전식 결제** 를 사용.

## 기술
- Java 21, Spring Boot 3, JPA (PostgreSQL), Redis(Redisson), JWT
- 테스트: JUnit5 + Testcontainers
