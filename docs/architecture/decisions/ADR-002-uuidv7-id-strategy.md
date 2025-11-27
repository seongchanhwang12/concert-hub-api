# ADDR-002 : 서비스 전역 식별자로 UUIDv7을 채택
## 1. Context
콘서트 예매시 부하분산을 위한 스케일 아웃을 가정한다. 
분산 환경에서 ID 충돌 위험을 최소화하고, DB 인덱스 효율을 최대화 해야한다. 
추후 성장으로 인한 대용량 트래픽 발생 시에도 안정적인 ID 생성이 필요하다.
DBMS 를 PostgreSQL 로 가정한다.

## 2. Decision 
서비스 전체 식별자로 UUIDv7을 사용하기로 결정.

## 3. Rationale 
- 시간 기반 정렬이 가능하여 UUID 대비 인덱스 효율적
- UUIDv4 대비 약 30~40% 부하 개선 
- 손쉽게 생성 가능 
- SnowFlake 대비 운영 비용 낮음(시간 동기화 문제 및 Worker ID 설정
- 시간순 정렬이므로 정렬된 저장에 대한 예측 가능성 향상

## 4. Alternatives Considered
### Snow Flake 
- 매우 빠름 
- 중앙 ID 서버 필요 -> 운영 복잡성 증가

### UUIDv4
- 가장 단순 
- 문자열 랜덤 -> 인덱스 파편화 문제(페이지 분할 증가, 엑세스 속도 저하)

### ULID 
- 간결하고 읽기 쉬움 
- 시간순 정렬 (생성 시간 기준)

## 5. Consequences
- DB 인덱스 효율 개선
- 모든 서비스 및 도메인 모델이 UUIDv7 기반으로 통일
- 일부 레거시 시스템과 호환성 검증 필요

## References
UUIDv7 vs SnowFlake : <https://velog.io/@sadik/UUID-v4-UUID-v7-Snowflake-ID-%EB%B9%84%EA%B5%90%ED%95%98%EA%B8%B0> \
UUIDv4 vs UUIDv7 vs ULID: 데이터베이스 성능에 적합한 식별자 선택 : <https://medium.com/@ciro-gomes-dev/uuidv4-vs-uuidv7-vs-ulid-choosing-the-right-identifier-for-database-performance-1f7d1a0fe0ba>