
# 콘서트 DB & ERD
-- -

## 모델링 원칙
-- -
### 테이블 history 컬럼
  - 데이터 생성/수정 일시 : createdAt(생성 일시) updatedAt(수정 일시)
  - 액터(사용자/시스템) : createdBy(소유자), updatedBy(수정자)
### PK 관련 규칙 
- 컬럼 자동생성 옵션 지양
- PK 포맷은 UUIDv7을 사용(세부 내용은 ADR-002 참고)
### 기타 컬럼 타입 규칙
- 포인트, 금액 관련 숫자 타입은 BIGINT 통일
- 시간 관련 타입은 TIMESTAMPTZ 통일 
- 상태 값은 VARCHAR 통일 (ENUM 사용 금지! 단, ERD 표기는 편의상 ENUM 으로 표기)
- seat.status 없음, 상태는 reservation / reservation_seat 에서 파생
- payment_history.idempotency_key 는
  동일 결제 요청 재시도 시 응답 재사용을 위한 키이며,
  (payment_id, idempotency_key) 는 유니크해야 한다
## ERD
--- -
```mermaid
erDiagram


    users {
        UUID id PK "사용자 고유 번호"
        VARCHAR(50) name  "사용자명"
        VARCHAR(100) email  "메일 주소"
        VARCHAR(50) password_hash  "비밀번호"
        VARCHAR(n) created_by "생성자"
        VARCHAR(n) updated_by "수정자"
        TIMESTAMPTZ created_at "생성 일시"
        TIMESTAMPTZ updated_at "수정 일시"
    }

    concert {
        UUID id PK "콘서트 고유 번호"
        VARCHAR(n) title  "콘서트 제목"
        VARCHAR(n) hall_name "콘서트 홀 명"
        VARCHAR(n) address  "주소"
        VARCHAR(n) artist  "아티스트명"
        VARCHAR(n) created_by "생성자"
        VARCHAR(n) updated_by "수정자"
        TIMESTAMPTZ created_at "생성 일시"
        TIMESTAMPTZ updated_at "수정 일시"
    }

    schedule {
        UUID id PK "스케줄 고유 번호"
        UUID concert_id FK "콘서트 번호"
        TIMESTAMPTZ start_at  "콘서트 시작 시간"
        TIMESTAMPTZ end_at "콘서트 종료 시간"
        VARCHAR(n) status  "일정 상태 - ON_SALE|CLOSED"
        VARCHAR(n) created_by "생성자"
        VARCHAR(n) updated_by "수정자"
        TIMESTAMPTZ created_at "생성 일시"
        TIMESTAMPTZ updated_at "수정 일시"
    }

    reservation {
        UUID id PK "예약 고유 번호"
        UUID user_id FK "사용자 고유 번호"
        UUID schedule_id FK "스케줄 고유 번호"
        VARCHAR(n) status  "예약 상태 - HELD|CONFIRMED|CANCELED|EXPIRED"
        VARCHAR(n) created_by "생성자"
        VARCHAR(n) updated_by "수정자"
        BIGINT  total_amount "예약 총액"
        TIMESTAMPTZ held_expired_at "홀드 만료시간"
        TIMESTAMPTZ created_at "생성 일시"
        TIMESTAMPTZ updated_at "수정 일시"
    }
    
    seat {
        UUID id PK "좌석 고유 번호"
        UUID schedule_id FK "스케줄 고유 번호"
        INTEGER row_number  "열 번호"
        INTEGER seat_number "좌석 번호"
        BIGINT price  "좌석 단가"
        VARCHAR(n) grade  "등급 - VIP|R|S"
        VARCHAR(n) section  "구역"
        TIMESTAMPTZ created_at "생성 일시"
        TIMESTAMPTZ updated_at "수정 일시"
        VARCHAR(n) created_by "생성자"
        VARCHAR(n) updated_by "수정자"
    }
    
    reservation_seat {
        UUID id PK "예약 좌석 고유 번호"
        UUID reservation_id FK "예약 고유 번호"
        UUID seat_id FK "좌석 ID"
        TIMESTAMPTZ created_at "생성 일시"
        TIMESTAMPTZ updated_at "수정 일시"
        VARCHAR(n) created_by "생성자"
        VARCHAR(n) updated_by "수정자"
    }

    payment {
        UUID id PK "결제 고유 키"
        UUID user_id FK "사용자 ID"
        UUID reservation_id FK "예약 ID"
        VARCHAR(n) method "POINT|CREDIT_CARD|BANK_TRANSFER"
        VARCHAR(n) status "SUCCESS|FAIL|CANCEL"
        BIGINT amount "총 결제 금액"
        VARCHAR(n) created_by "생성자"
        VARCHAR(n) updated_by "수정자"
        TIMESTAMPTZ created_at "생성 일시"
        TIMESTAMPTZ updated_at "수정 일시"
    }

    payment_history {
        UUID id PK "결제 내역 고유 번호"
        UUID payment_id FK "결제 고유 번호"
        BIGINT amount  "결제 금액"
        VARCHAR(n) method  "결제 방식 - POINT|CREDIT_CARD|BANK_TRANSFER"
        VARCHAR(n) status  "결제 상태 - SUCCESS|FAIL|CANCEL"
        TIMESTAMPTZ paid_at  "결제 일시"
        TIMESTAMPTZ created_at "생성 일시"
        VARCHAR(n) created_by "생성자"
        UUID idempotency_key "멱등성 KEY"
    }
    
    user_point {
        UUID id PK "포인트 잔액 PK"
        UUID user_id FK "사용자 고유번호"
        BIGINT balance "현재 포인트 잔액"
        TIMESTAMPTZ created_at
        TIMESTAMPTZ updated_at
        VARCHAR(n) created_by
        VARCHAR(n) updated_by        
    }

    point_history {
        UUID id PK "포인트 내역 고유 번호"
        UUID point_id FK "포인트 고유번호"
        UUID user_id  "사용자 고유 번호"
        BIGINT amount  "포인트 액수"
        VARCHAR(n) type  "유형 - CHARGE|USE|EARN"
        TIMESTAMPTZ created_at "생성 일시"
        VARCHAR(n) created_by "생성자"
        UUID idempotency_key
    }
    
    queue_ticket{
        UUID id PK "대기열 고유 번호"
        UUID user_id "사용자 고유 번호"
        UUID shcedule_id "스케줄 고유 번호"
        UUID token "대기열 토큰 번호"
        enum status "WAITING|ACTIVE|EXPIRED|CANCELED"
        BIGINT position_at_activation "대기열 순번"
        TIMESTAMPTZ created_at "토큰 발행일자"
        TIMESTAMPTZ activated_at "토큰 활성 일시"
        TIMESTAMPTZ expired_at "토큰 만료 일시"
    }

    users ||--o{ reservation : "makes"
    users ||--o{ payment : "makes"
    users ||--|| user_point : "has"
    users ||--|{ point_history : "has"

    payment ||--|{ payment_history : "has"

    concert ||--o{ schedule : "has"
    schedule ||--|{ seat : "has"
    schedule ||--o{ reservation : "has"

    reservation ||--o| payment : "pays"
    reservation ||--|{ reservation_seat : "includes"
    user_point ||--|{ point_history : "logs"
    seat ||--o{ reservation_seat : "reserved_by"
    
    users ||--o{ queue_ticket : enters 
    schedule ||--o{ queue_ticket :has 
```