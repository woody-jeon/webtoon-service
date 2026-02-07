# 웹툰 회차 구매 시스템
웹툰 플랫폼에서 사용자가 특정 회차를 구매하고 열람할 수 있는 REST API 시스템입니다.

## 목차
- [프로젝트 개요](#프로젝트-개요)
- [기술 스택](#기술-스택)
- [아키텍처 및 설계 의도](#아키텍처-및-설계-의도)
- [DB 설계](#db-설계)
- [API 명세서](#api-명세서)
- [AI 사용 관련 설명](#ai-사용-관련-설명)
- [폴더 구조](#폴더-구조)

---

## 프로젝트 개요

### 핵심 기능
- **회차 구매**: 사용자가 코인을 사용하여 웹툰 회차를 구매
- **콘텐츠 열람**: 구매한 회차의 콘텐츠를 조회하고 열람
- **중복 결제 방지**: DB Unique Constraint와 Pessimistic Lock을 활용한 동시성 제어
- **멱등성 보장**: Idempotency Key를 통한 네트워크 재시도 대응

### 주요 특징
- 멀티 모듈 레이어드 아키텍처로 비즈니스 로직과 인프라 계층 분리

---

## 기술 스택

### Language & Framework
- **Kotlin** 1.9.24
- **Spring Boot** 3.3.1
- **Spring Data JPA** + Hibernate
- **QueryDSL** 5.0.0

### Database
- **MySQL** 8.0+ 
- **H2** (Test)

### Build Tool
- **Gradle** 8.x (Kotlin DSL)

### Architecture Pattern
- **Multi-Module Layered Architecture**
- **Domain-Driven Design (DDD) 일부 적용**

---

## 아키텍처 및 설계 의도

### 멀티 모듈 레이어드 아키텍처

```
┌─────────────────────────────────────────────────────────┐
│                   Presentation Layer                    │
│              (core-api: Controllers, DTOs)              │
└───────────────────────┬─────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────┐
│                    Domain Layer                         │
│     (core-domain: Services, Functions, Models)          │
│                                                         │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │   Service    │→ │   Function   │→ │  Repository  │   │
│  │  (Use Case)  │  │  (Business)  │  │  Interface   │   │
│  └──────────────┘  └──────────────┘  └──────────────┘   │
└───────────────────────┬─────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────┐
│                  Infrastructure Layer                   │
│         (db-core: Entities, JPA, Repository Impl)       │
└─────────────────────────────────────────────────────────┘
```

### 설계 의도

#### 1. 멀티 모듈 구조
각 모듈은 명확한 책임을 가지며, 의존성 방향을 단방향으로 유지합니다.

- **core-api**: 외부 요청을 받고 응답하는 계층 (Controller, DTO)
- **core-domain**: 비즈니스 로직의 핵심 (Service, Function, Domain Model, Repository Interface)
- **core-enum**: 공통 Enum 정의
- **storage/db-core**: 데이터베이스 접근 계층 (Entity, JPA Repository, Repository 구현체)
- **support/logging**: 횡단 관심사 (로깅, 모니터링)

**의존성 방향**
```
core-api → core-domain → core-enum
              ↑
         db-core (Repository 구현체가 인터페이스 구현)
```

#### 2. 계층별 역할 분리

**Service (Orchestrator)**
- 여러 Function을 조합하여 서비스 로직 구현

**Function (Business Logic)**
- 단일 책임을 가진 비즈니스 로직 단위
- 재사용 가능한 도메인 기능

**Repository (인터페이스 & 구현체 분리)**
- 인터페이스: 도메인 계층(core-domain)에 정의하여 비즈니스 로직이 인프라에 의존하지 않도록 함
- 구현체: 인프라 계층(db-core)에서 JPA를 사용하여 실제 데이터베이스 접근 구현
- 의존성 역전 원칙(DIP) 적용으로 테스트 용이성과 유연성 확보

### 3. 동시성 제어 전략

#### 목표
동시에 여러 결제 요청이 유입되는 상황에서도 중복 결제 및 잔액 오류 없이 결제를 안전하게 처리하는 것을 목표로 합니다.

#### 설계 요약

이 과제에서는 DB를 중심으로 데이터를 관리하며, 다음 조합을 통해 동시성을 제어합니다.

- **DB Unique Constraint**
- **Pessimistic Lock (SELECT ... FOR UPDATE)**

결제 도메인의 특성상 트랜잭션 정합성과 원자성 보장이 가장 중요하다고 판단하여 DB 트랜잭션 기반의 방식으로 설계했습니다.

#### 선택한 전략

##### 1. DB Unique Constraint
- 목적: 중복 결제의 최종 차단
- 방식:
   - `UNIQUE(user_id, episode_id)` 제약
- 효과:
   - 애플리케이션 로직 오류가 있더라도 DB 레벨에서 중복 결제 방지
   - INSERT 시점에 원자적으로 검증

##### 2. Pessimistic Lock
- 목적: 사용자 잔액의 동시 차감 방지
- 방식:
   - `SELECT ... FOR UPDATE`로 User 레코드에 배타 락 적용
- 효과:
   - 동일 사용자의 동시 결제 요청을 순차 처리
   - 잔액 부족 및 이중 차감 방지
- 범위:
   - User 레코드만 락하여 락 범위를 최소화

---

#### 동작 흐름 (요약)

1. 트랜잭션 시작
2. User 레코드에 Pessimistic Lock 획득
3. 잔액 검증
4. Purchase INSERT
   - 중복인 경우 DB Unique Constraint에 의해 예외 발생
5. 잔액 차감
6. 트랜잭션 커밋

→ 결과적으로 잔액은 한 번만 차감되며, 중복 구매는 DB에서 차단

#### 실패 및 롤백 처리

결제 처리 중 어느 단계에서든 예외가 발생할 경우, 트랜잭션 롤백

- 잔액 부족
- 중복 구매로 인한 Unique Constraint 위반
- 기타 결제 처리 오류

이 경우 구매 내역(purchases)과 열람 권한(user_episode_accesses)은 생성되지 않으며,
사용자는 동일 회차에 대해 재시도가 가능

이를 통해 실패한 결제 시도가 데이터 정합성에 영향을 주지 않도록 설계

---

#### 3. Redis 사용하지 않은 이유

과제 환경에서는 단일 DB를 사용하며, 결제 도메인 특성상 DB 트랜잭션과 완전히 일관된 제어 방식이 더 적합하다고 판단
- DB 트랜잭션과 자연스럽게 통합 가능
- 별도의 인프라 없이도 충분한 정합성 보장
- 장애 및 예외 상황에서 DB가 자동으로 롤백 처리
- 구현 복잡도 줄이고 안정성 우선하는 DB 기반 동시성 제어 방식

#### 4. 중복 키 예외 처리 전략

**문제**
- 결제 생성 시 DB Unique Constraint에 의해 중복 구매가 차단됨
- 그러나 동일 트랜잭션 내에서는 아직 커밋되지 않은 데이터만 조회 가능하여, 이미 완료된 결제인지 여부를 즉시 판단하기 어려움

**해결 전략**
- 중복 예외 발생 시, 트랜잭션 경계를 분리하여 이미 커밋된 결제 데이터만 조회하도록 설계

**처리 방식**
- 중복 예외 발생 → 별도의 트랜잭션에서 재조회
   - 동일 Idempotency Key인 경우: 기존 결제 결과 반환 (멱등성 보장)
   - 다른 요청으로 인한 중복 구매인 경우: 이미 구매됨 에러 반환

이를 통해:
- DB Unique Constraint를 최종 방어선으로 유지
- 트랜잭션 격리 수준에 따른 조회 한계를 우회
- 중복 요청과 정상 실패 케이스를 명확히 구분

---

## DB 설계

### 1. 테이블 생성 (DDL)

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
    coin DECIMAL(12, 2) NOT NULL DEFAULT 0.00 COMMENT 'coin',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='사용자';

CREATE TABLE episodes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
    webtoon_id BIGINT NOT NULL COMMENT '웹툰 ID',
    episode_number INT NOT NULL COMMENT '회차 번호',
    title VARCHAR(200) NOT NULL COMMENT '회차 제목',
    price DECIMAL(10, 2) NOT NULL COMMENT '가격',
    thumbnail VARCHAR(500) NULL COMMENT '썸네일 이미지 URL',
    content_data JSON NULL COMMENT '회차 콘텐츠',
    is_available BOOLEAN NOT NULL DEFAULT TRUE COMMENT '구매 가능 여부',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',

    INDEX idx_webtoon_episode (webtoon_id, episode_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='웹툰 회차';

CREATE TABLE purchases (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
    user_id BIGINT NOT NULL COMMENT '사용자 ID',
    episode_id BIGINT NOT NULL COMMENT '회차 ID',
    idempotency_key VARCHAR(100) NOT NULL COMMENT '멱등성 키',
    idempotency_expires_at DATETIME NOT NULL COMMENT '멱등성 키 만료 시간',
    amount DECIMAL(10, 2) NOT NULL COMMENT '결제 금액',
    status VARCHAR(20) NOT NULL COMMENT '결제 상태',
    completed_at DATETIME NULL COMMENT '완료 일시',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    UNIQUE KEY uk_user_episode (user_id, episode_id),
    UNIQUE KEY uk_idempotency (idempotency_key),
    INDEX idx_user_created (user_id, created_at),
    INDEX idx_idempotency_expires (idempotency_key, idempotency_expires_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='구매 내역';

CREATE TABLE user_episode_accesses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
    user_id BIGINT NOT NULL COMMENT '사용자 ID',
    episode_id BIGINT NOT NULL COMMENT '회차 ID',
    purchase_id BIGINT NOT NULL COMMENT '구매 ID',
    granted_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '권한 부여 일시',
    expires_at DATETIME NULL COMMENT '만료 일시',
    last_accessed_at DATETIME NULL COMMENT '마지막 접근 일시',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    UNIQUE KEY uk_user_episode_access (user_id, episode_id),
    INDEX idx_user_expires (user_id, expires_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='사용자 회차 접근 권한';

`purchases` 테이블은 결제 "결과"를 저장하는 테이블로 사용하며, 결제 실패 시에는 트랜잭션이 롤백되며, FAILED 상태의 레코드는 저장하지 않는다.
status 컬럼은 트랜잭션 처리 중 상태 표현 및 확장 가능성을 고려
```

### 2. 샘플 데이터 삽입

```sql
-- 테스트 사용자 (잔액 보유)
INSERT INTO users (id, coin, created_at, updated_at) VALUES
(1, 1000.00, NOW(), NOW()),   -- 충분한 잔액
(2, 100.00, NOW(), NOW()),    -- 적은 잔액 (잔액 부족 테스트용)
(3, 0.00, NOW(), NOW());      -- 잔액 없음

INSERT INTO episodes (id, webtoon_id, episode_number, title, price, thumbnail, content_data, is_available, created_at, updated_at) VALUES
(1, 1, 1, '1화', 0.00, 'https://cdn.example.com/webtoon/1/ep/1/thumb.jpg', '{"content": ["https://cdn.example.com/webtoon/1/ep/1/page1.jpg", "https://cdn.example.com/webtoon/1/ep/1/page2.jpg", "https://cdn.example.com/webtoon/1/ep/1/page3.jpg"]}', TRUE, NOW(), NOW()),      -- 무료 회차
(2, 1, 2, '2화', 300.00, 'https://cdn.example.com/webtoon/1/ep/2/thumb.jpg', '{"content": ["https://cdn.example.com/webtoon/1/ep/2/page1.jpg", "https://cdn.example.com/webtoon/1/ep/2/page2.jpg", "https://cdn.example.com/webtoon/1/ep/2/page3.jpg"]}', TRUE, NOW(), NOW()),   -- 유료 회차
(3, 1, 3, '3화', 300.00, 'https://cdn.example.com/webtoon/1/ep/3/thumb.jpg', '{"content": ["https://cdn.example.com/webtoon/1/ep/3/page1.jpg", "https://cdn.example.com/webtoon/1/ep/3/page2.jpg", "https://cdn.example.com/webtoon/1/ep/3/page3.jpg"]}', TRUE, NOW(), NOW()),   -- 유료 회차
(4, 1, 4, '4화', 300.00, 'https://cdn.example.com/webtoon/1/ep/4/thumb.jpg', '{"content": ["https://cdn.example.com/webtoon/1/ep/4/page1.jpg", "https://cdn.example.com/webtoon/1/ep/4/page2.jpg", "https://cdn.example.com/webtoon/1/ep/4/page3.jpg"]}', FALSE, NOW(), NOW()); -- 구매 불가 (미공개)

INSERT INTO episodes (id, webtoon_id, episode_number, title, price, thumbnail, content_data, is_available, created_at, updated_at) VALUES
(5, 2, 1, '1화', 0.00, 'https://cdn.example.com/webtoon/2/ep/1/thumb.jpg', '{"content": ["https://cdn.example.com/webtoon/2/ep/1/page1.jpg", "https://cdn.example.com/webtoon/2/ep/1/page2.jpg"]}', TRUE, NOW(), NOW()),      -- 무료 회차
(6, 2, 2, '2화', 200.00, 'https://cdn.example.com/webtoon/2/ep/2/thumb.jpg', '{"content": ["https://cdn.example.com/webtoon/2/ep/2/page1.jpg", "https://cdn.example.com/webtoon/2/ep/2/page2.jpg"]}', TRUE, NOW(), NOW()),   -- 유료 회차
(7, 2, 3, '3화', 200.00, 'https://cdn.example.com/webtoon/2/ep/3/thumb.jpg', '{"content": ["https://cdn.example.com/webtoon/2/ep/3/page1.jpg", "https://cdn.example.com/webtoon/2/ep/3/page2.jpg"]}', TRUE, NOW(), NOW());   -- 유료 회차
```

---

## API 명세서

### 1. 회차 구매 API

#### 기본 정보
| 항목 | 내용 |
|------|------|
| Method | `POST` |
| Endpoint | `/api/v1/users/{userId}/episodes/{episodeId}/purchase` |
| Description | 사용자가 코인으로 특정 회차를 구매하고 열람 권한을 획득 |
| Content-Type | `application/json` |

#### Request

**Path Parameters**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| userId | Long | Yes | 사용자 ID |
| episodeId | Long | Yes | 구매할 회차 ID |

**Headers**
| Header | Type | Required | Description |
|--------|------|----------|-------------|
| Idempotency-Key | String | Yes | 중복 요청 방지용 고유 키 (최대 100자) |

#### Response

**Success (200 OK)**
```json
{
  "result": "SUCCESS",
  "data": {
    "purchase_id": 1,
    "user_id": 1,
    "episode_id": 1,
    "amount": 300.00,
    "purchased_at": "2026-02-07T15:30:00",
    "status": "COMPLETED"
  },
  "error": null
}
```

**Error Responses**

| HTTP Status | Error Code | Message | 설명 |
|-------------|------------|---------|------|
| 200 | WEBTOON_1002 | 구매할 수 없는 회차입니다. | 회차가 판매 중지 상태 |
| 200 | WEBTOON_1003 | 이미 구매한 회차입니다. | 중복 구매 시도 |
| 200 | WEBTOON_1004 | 코인이 부족합니다. | 잔액 부족 |
| 200 | WEBTOON_1008 | 멱등성 키가 다른 구매 요청에 이미 사용되었습니다. | Idempotency Key 불일치 |
| 404 | WEBTOON_1001 | 존재하지 않는 사용자입니다. | 사용자 없음 |
| 404 | WEBTOON_2001 | 존재하지 않는 회차입니다. | 회차 없음 |
| 500 | WEBTOON_1007 | 구매 처리 중 오류가 발생했습니다. | 내부 서버 오류 |

---

### 2. 회차 열람 API

#### 기본 정보
| 항목 | 내용 |
|------|------|
| Method | `GET` |
| Endpoint | `/api/v1/users/{userId}/episodes/{episodeId}/content` |
| Description | 구매한 회차의 콘텐츠를 조회하고 마지막 열람 시간을 갱신 |
| Content-Type | `application/json` |

#### Request

**Path Parameters**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| userId | Long | Yes | 사용자 ID |
| episodeId | Long | Yes | 열람할 회차 ID |

#### Response

**Success (200 OK)**
```json
{
  "result": "SUCCESS",
  "data": {
    "episode_id": 1,
    "title": "1화: 시작",
    "thumbnail": "https://cdn.example.com/episodes/1/thumbnail.jpg",
    "content_pages": [
      "https://cdn.example.com/episodes/1/page001.jpg",
      "https://cdn.example.com/episodes/1/page002.jpg",
      "https://cdn.example.com/episodes/1/page003.jpg"
    ],
    "accessed_at": "2026-02-07T15:35:00"
  },
  "error": null
}
```

**Error Responses**

| HTTP Status | Error Code | Message | 설명 |
|-------------|------------|---------|------|
| 200         | WEBTOON_1005 | 구매하지 않은 회차입니다. | 접근 권한 없음 |
| 200         | WEBTOON_1006 | 열람 기간이 만료되었습니다. | 열람 기간 만료 |
| 500         | WEBTOON_1001 | 존재하지 않는 사용자입니다. | 사용자 없음 |
| 500         | WEBTOON_2001 | 존재하지 않는 회차입니다. | 회차 없음 |

---

## AI 사용 관련 설명
AI는 주로 리스크 점검과 대안 검토 목적으로 사용했습니다.

---

#### 사용한 AI 도구
- ChatGPT

---

#### 최초 프롬프트
결제 시스템 과제를 구현하려고 하는데 동시에 여러 요청이 들어올 때 동시성 제어 전략을 설계하고 있어, 
DB 트랜잭션 기반으로 user_id, episode_id 유니크 키 걸고 DB 락만 써도 될 거 같긴한데 
더 생각해야될 게 있을까? redis는 안쓰고 싶긴 한데, 써야되면 사용해도 좋아

---

#### AI 결과에 대한 본인 리뷰

**AI가 유용했던 부분**
- DB Unique Constraint, Pessimistic Lock, Redis 등 동시성 제어 전략의 일반적인 선택지를 빠르게 정리하는 데 도움을 받음
- 각 방식의 장단점을 비교하면서, 설계 시 고려해야 할 리스크를 점검에 엄청 도움되었음
- 메서드 네이밍 직관적으로 변경

**AI가 부정확하거나 실무적으로 그대로 적용하기 어려웠던 부분**
- Redis 분산 락을 결제 도메인에도 일반적으로 적용 가능한 것처럼 제안
- Optimistic Lock을 단순히 “성능이 좋다”는 이유로 추천
- 트랜잭션 격리 수준, 커밋 시점에 따른 실제 동작 차이에 대한 설명이 추상적인 경우가 있었음
- '이렇게 하는게 맞지 않냐?'의 톤으로 물어보는 건 맞다고 말하고 보는 부분 

**본인이 수정·보완한 내용**
- 결제 도메인의 특성상 트랜잭션 정합성이 중요하다고 판단하여, Redis 대신 DB를 중심으로 데이터를 관리하는 방향으로 설계
- Optimistic Lock은 충돌 시 재시도 비용과 예외 처리가 복잡해질 수 있어, 과제의 요구사항에는 Pessimistic Lock이 더 적합하다고 판단

**최종 결과에서 본인의 기여**
- 결제 도메인에 맞는 동시성 제어 전략을 설계하고, 각 선택에 대해 발생 가능한 리스크를 검토한 뒤 트레이드오프를 명확히 함
- DB Unique Constraint를 최종 방어선으로 두고, 락 범위를 최소화하여 정합성과 단순성을 동시에 만족하는 구조로 정리

---

## 폴더 구조

```
webtoon-service/
├── core/
│   ├── core-api/                          # Presentation Layer
│   │   └── src/main/kotlin/.../core/api/
│   │       ├── controller/
│   │       │   └── episode/
│   │       │       ├── EpisodePurchaseController.kt
│   │       │       └── EpisodeContentController.kt
│   │       ├── dto/
│   │       │   └── response/
│   │       │       ├── PurchaseResponse.kt
│   │       │       └── EpisodeContentResponse.kt
│   │       └── support/
│   │           ├── error/                 # API 에러 코드 및 타입
│   │           ├── response/              # 통합 응답 래퍼
│   │           └── utils/                 # 유틸리티
│   │
│   ├── core-domain/                       # Application Layer
│   │   └── src/main/kotlin/.../core/domain/
│   │       ├── episode/
│   │       │   ├── model/                 # 도메인 모델
│   │       │   │   ├── Episode.kt
│   │       │   │   ├── EpisodeContent.kt
│   │       │   │   ├── UserEpisodeAccess.kt
│   │       │   │   ├── EpisodeId.kt       # Value Object
│   │       │   │   ├── UserEpisode.kt     # Value Object
│   │       │   │   └── repository/        # Repository 인터페이스
│   │       │   └── service/
│   │       │       ├── EpisodeContentService.kt
│   │       │       └── function/          # 비즈니스 로직 단위
│   │       │           ├── EpisodeFinder.kt
│   │       │           ├── UserEpisodeAccessCreator.kt
│   │       │           ├── UserEpisodeAccessModifier.kt
│   │       │           └── UserEpisodeAccessValidator.kt
│   │       │
│   │       ├── purchase/
│   │       │   ├── model/
│   │       │   │   ├── Purchase.kt
│   │       │   │   ├── IdempotencyKey.kt  # Value Object
│   │       │   │   ├── PurchaseId.kt      # Value Object
│   │       │   │   └── repository/        # Repository 인터페이스
│   │       │   └── service/
│   │       │       ├── EpisodePurchaseService.kt
│   │       │       ├── function/
│   │       │       │   ├── PurchaseCreator.kt
│   │       │       │   ├── PurchaseFinder.kt
│   │       │       │   └── PurchaseValidator.kt
│   │       │       └── handler/
│   │       │           └── PurchaseDuplicateHandler.kt
│   │       │
│   │       ├── user/
│   │       │   ├── model/
│   │       │   │   ├── User.kt
│   │       │   │   ├── UserId.kt          # Value Object
│   │       │   │   └── repository/        # Repository 인터페이스
│   │       │   └── service/
│   │       │       └── function/
│   │       │           ├── UserFinder.kt
│   │       │           └── UserModifier.kt
│   │       │
│   │       └── support/
│   │           └── error/                 # 도메인 에러 정의
│   │               ├── DomainErrorType.kt
│   │               ├── DomainErrorCode.kt
│   │               └── DomainErrorMessage.kt
│   │
│   └── core-enum/                         # Shared Enums
│       └── src/main/kotlin/.../enums/
│           └── PurchaseStatus.kt
│
├── storage/
│   └── db-core/                           # Infrastructure Layer
│       └── src/main/kotlin/.../storage/db/core/
│           ├── config/                    # DB 설정
│           ├── episode/
│           │   ├── entity/
│           │   │   ├── EpisodeEntity.kt
│           │   │   └── UserEpisodeAccessEntity.kt
│           │   └── repository/            # Repository 구현체
│           │       ├── EpisodeEntityJpaRepository.kt
│           │       ├── EpisodeEntityRepository.kt
│           │       ├── EpisodeEntityQueryRepository.kt
│           │       └── ...
│           ├── purchase/
│           │   ├── entity/
│           │   │   └── PurchaseEntity.kt
│           │   └── repository/
│           │       ├── PurchaseEntityJpaRepository.kt
│           │       ├── PurchaseEntityRepository.kt
│           │       └── PurchaseEntityQueryRepository.kt
│           └── user/
│               ├── entity/
│               │   └── UserEntity.kt
│               └── repository/
│                   ├── UserEntityJpaRepository.kt
│                   ├── UserEntityRepository.kt
│                   └── UserEntityQueryRepository.kt
│
└── support/
    └── logging/                           # Cross-cutting Concerns
```