# Rebook Chat Service

<div align="center">

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.13-brightgreen)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2023.0.5-blue)
![Java](https://img.shields.io/badge/Java-17-orange)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14+-336791)
![MongoDB](https://img.shields.io/badge/MongoDB-5.x-47A248)
![Redis](https://img.shields.io/badge/Redis-6+-DC382D)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3.x-FF6600)
![Gradle](https://img.shields.io/badge/Gradle-8.14.2-02303A)

**Rebook 마이크로서비스 아키텍처의 실시간 채팅 서비스**

WebSocket/STOMP 기반 실시간 메시징과 알림 발행을 담당하는 핵심 백엔드 서비스

</div>

---

## 1. 개요

**Rebook Chat Service**는 중고 도서 거래 플랫폼 Rebook의 실시간 채팅 마이크로서비스로, 사용자 간 실시간 메시징을 제공합니다. Spring Boot와 WebSocket/STOMP 프로토콜 기반으로 구현된 본 서비스는 **서비스 디스커버리**, **중앙화된 설정 관리**, **비동기 메시징**을 통한 확장 가능한 구조를 제공합니다.


### 서비스 역할

본 서비스는 Rebook 플랫폼 내에서 다음과 같은 역할을 담당합니다:

- **실시간 채팅**: WebSocket/STOMP 기반 양방향 실시간 메시징
- **채팅방 관리**: 거래 기반 1:1 채팅방 생성 및 관리
- **메시지 히스토리**: MongoDB 기반 채팅 메시지 영구 저장 및 조회
- **읽음 상태 관리**: 사용자별 읽음 상태 추적 및 미확인 메시지 카운트
- **알림 발행**: RabbitMQ를 통한 실시간 채팅 알림 전송

---

## 2. 목차
- [주요 기능](#3-주요-기능)
- [기술 스택](#4-기술-스택)
- [아키텍처](#5-아키텍처)
- [API 문서](#6-api-문서)
- [프로젝트 구조](#7-프로젝트-구조)

---

## 3. 주요 기능

### 3.1 실시간 메시징

#### WebSocket 기반 양방향 통신
- ✅ STOMP 프로토콜을 통한 메시지 라우팅
- ✅ SockJS 폴백으로 브라우저 호환성 보장
- ✅ `/ws-chat` 엔드포인트로 WebSocket 연결
- ✅ 구독자 기반 메시지 브로드캐스팅

#### 메시지 전송 및 수신
- ✅ 실시간 채팅 메시지 전송 (`/app/api/chats/message`)
- ✅ 채팅방별 메시지 구독 (`/topic/room/{roomId}`)
- ✅ 사용자 입장/퇴장 이벤트 브로드캐스팅
- ✅ MongoDB 기반 메시지 영구 저장

#### 메시지 히스토리
- ✅ 채팅 메시지 히스토리 조회 (페이지네이션 지원)
- ✅ 최신순/오래된 순 정렬 옵션
- ✅ 효율적인 인덱스 설계로 빠른 조회 성능
- ✅ 문서형 데이터베이스(MongoDB) 활용

### 3.2 채팅방 관리

- ✅ 거래(trading) 기반 1:1 채팅방 자동 생성
- ✅ 채팅방 중복 생성 방지 (동일 사용자 쌍 검증)
- ✅ 채팅방 상세 정보 조회 (참여자, 거래 정보)
- ✅ 사용자별 채팅방 목록 조회 (페이지네이션 지원)
- ✅ 채팅방 삭제 (소유자 권한 검증)

### 3.3 읽음 상태 관리

- ✅ 사용자별 마지막 읽은 시간 추적
- ✅ 읽지 않은 메시지 수 실시간 계산
- ✅ 읽음 상태 업데이트 API 제공
- ✅ 복합키(room_id + user_id) 기반 효율적인 상태 관리
- ✅ 채팅방별/전체 미확인 메시지 카운트 조회

### 3.4 알림 시스템 (Event-Driven)

- ✅ 새로운 메시지 수신 시 실시간 알림 발행
- ✅ RabbitMQ 기반 비동기 메시징 (Notification Service 연동)
- ✅ JSON 메시지 직렬화를 통한 안정적 메시지 전송
- ✅ `chat.notification.queue` 큐를 통한 알림 전송


---

## 4. 기술 스택

### 4.1 백엔드 프레임워크

| 기술 | 버전 | 용도 |
|------|------|------|
| **Spring Boot** | 3.3.13 | 애플리케이션 프레임워크 |
| **Java** | 17 | 프로그래밍 언어 |
| **Spring Data JPA** | - | ORM 및 데이터 접근 계층 (PostgreSQL) |
| **Spring Data MongoDB** | - | MongoDB 데이터 접근 계층 |
| **Spring Validation** | - | 요청 데이터 유효성 검증 |
| **Lombok** | - | 보일러플레이트 코드 제거 |

### 4.2 실시간 통신

| 기술 | 용도 |
|------|------|
| **Spring WebSocket** | WebSocket 연결 관리 |
| **STOMP** | 메시지 프로토콜 및 라우팅 |
| **SockJS** | WebSocket 폴백 지원 |

### 4.3 데이터베이스 & 캐싱

| 기술 | 버전 | 용도 |
|------|------|------|
| **PostgreSQL** | 14+ | 관계형 데이터베이스 (채팅방, 읽음 상태) |
| **MongoDB** | 5.x | 문서형 데이터베이스 (채팅 메시지) |
| **Redis** | 6+ | 분산 캐싱 및 세션 관리 |

### 4.4 마이크로서비스 인프라 (Spring Cloud)

| 기술 | 버전 | 용도 |
|------|------|------|
| **Eureka Client** | 2023.0.5 | 서비스 디스커버리 및 등록 |
| **Spring Cloud Config** | 2023.0.5 | 중앙화된 설정 관리 (외부 Config Server) |
| **OpenFeign** | 2023.0.5 | 선언적 HTTP 클라이언트 (서비스 간 통신) |

### 4.5 메시징 & 이벤트

| 기술 | 버전 | 용도 |
|------|------|------|
| **RabbitMQ (AMQP)** | 3.x | 비동기 메시징 및 이벤트 발행 |
| **Spring AMQP** | - | RabbitMQ 통합 및 메시지 컨버터 |

### 4.6 모니터링 & 로깅

| 기술 | 버전 | 용도 |
|------|------|------|
| **Spring Actuator** | - | 헬스체크 및 메트릭 엔드포인트 |
| **Prometheus** | - | 메트릭 수집 및 모니터링 |
| **Sentry** | 8.13.2 | 실시간 에러 트래킹 및 알림 |
| **SLF4J & Logback** | - | 애플리케이션 로깅 |

### 4.7 API 문서화

| 기술 | 버전 | 용도 |
|------|------|------|
| **SpringDoc OpenAPI 3** | 2.6.0 | Swagger UI 기반 REST API 문서 자동 생성 |

### 4.8 빌드 & 배포

| 기술 | 버전 | 용도 |
|------|------|------|
| **Gradle** | 8.14.2 | 빌드 자동화 도구 |
| **Docker** | - | 컨테이너화 및 배포 |
| **Jacoco** | - | 테스트 커버리지 분석 |
| **JUnit 5** | - | 단위 테스트 프레임워크 |

---

## 5. 아키텍처

### 5.1 마이크로서비스 아키텍처

```
┌─────────────────┐
│  API Gateway    │ ← 인증 및 라우팅 (X-User-Id 헤더 주입)
└────────┬────────┘
         │
         ├──────────────┬──────────────┬──────────────┐
         │              │              │              │
    ┌────▼────┐    ┌────▼────┐   ┌────▼────┐    ┌────▼────┐
    │  Chat   │    │ Trading │   │  User   │    │ Notif.  │
    │ Service │    │ Service │   │ Service │    │ Service │
    └────┬────┘    └─────────┘   └─────────┘    └────▲────┘
         │                                            │
         │         ┌──────────────┐                   │
         └────────►│  RabbitMQ    │───────────────────┘
                   │  (AMQP)      │  (Chat Notifications)
                   └──────────────┘

    ┌─────────────────────────────────────────────┐
    │        Infrastructure Services              │
    ├─────────────────────────────────────────────┤
    │  Eureka  │  Config Server  │  PostgreSQL   │
    │  Redis   │  MongoDB        │  Prometheus   │
    └─────────────────────────────────────────────┘
```
### 5.3 RabbitMQ STOMP 아키텍처
![STOMP](https://rebook-bucket.s3.ap-northeast-2.amazonaws.com/rebook/rabbitmq_stomp.png)
### 5.3 실시간 채팅 흐름

![메세지플로우](https://rebook-bucket.s3.ap-northeast-2.amazonaws.com/rebook/chatflow.png)

### 5.4 엔티티 관계도 (ERD)

```
┌─────────────────────────────┐
│        ChatRoom             │
├─────────────────────────────┤
│ PK  id (Long)               │
│     tradingId (Long)        │
│     user1Id (String)        │
│     user2Id (String)        │
│     createdAt (LocalDateTime)│
│     updatedAt (LocalDateTime)│
└─────────────────────────────┘
              │
              │ 1:N
              │
              ▼
┌─────────────────────────────┐
│      ChatReadStatus         │  (읽음 상태)
├─────────────────────────────┤
│ PK  roomId (Long)           │ ← Composite Key
│ PK  userId (String)         │ ← Composite Key
│     lastReadAt (LocalDateTime)│
│     createdAt (LocalDateTime)│
│     updatedAt (LocalDateTime)│
└─────────────────────────────┘

┌─────────────────────────────┐
│      ChatMessage            │  (MongoDB 컬렉션)
├─────────────────────────────┤
│ _id (ObjectId)              │
│ type (MessageType enum)     │
│ roomId (Long)               │
│ senderId (String)           │
│ message (String)            │
│ sendAt (LocalDateTime)      │
└─────────────────────────────┘
```

**엔티티 설계 패턴**:
- **JPA Auditing**: `@EntityListeners(AuditingEntityListener.class)`로 생성/수정 시간 자동 관리
- **복합키 (Composite Key)**: `ChatReadStatusId` 임베디드 클래스로 사용자-채팅방 관계 표현
- **하이브리드 스토리지**: PostgreSQL (구조화된 데이터) + MongoDB (메시지 데이터)
- **메시지 타입**: `MessageType` enum (ENTER, CHAT, LEAVE)


## 6. API 문서

### 6.1 API 엔드포인트 상세

#### 6.1.1 채팅방 관리 API (`ChatRoomController`)

| Method | Endpoint | Summary |
|--------|----------|---------|
| **POST** | `/api/chats/room` | 채팅방 생성 |
| **GET** | `/api/chats/room/{roomId}` | 채팅방 상세 조회 |
| **GET** | `/api/chats/rooms` | 사용자의 채팅방 목록 조회 |
| **DELETE** | `/api/chats/room/{roomId}` | 채팅방 삭제 |

#### 6.1.2 메시지 관리 API (`ChatMessageController`)

| Method | Endpoint | Summary |
|--------|----------|---------|
| **GET** | `/api/chats/messages/{roomId}` | 메시지 히스토리 조회 (페이징) |
| **POST** | `/api/chats/message` | 메시지 전송 (REST) |

#### 6.1.3 읽음 상태 API (`ChatReadStatusController`)

| Method | Endpoint | Summary |
|--------|----------|---------|
| **PUT** | `/api/chats/read/{roomId}` | 읽음 상태 업데이트 |
| **GET** | `/api/chats/unread/count` | 읽지 않은 메시지 수 조회 |

### 6.2 WebSocket/STOMP 엔드포인트

#### 연결
```
WebSocket Endpoint: /ws-chat
SockJS Endpoint: /ws-chat (with fallback)
```

#### 구독 (Subscribe)
```
/topic/room/{roomId}                  # 특정 채팅방 메시지 수신
```

#### 발행 (Send)
```
/app/api/chats/message                # 메시지 전송
/app/api/chats/enter                  # 채팅방 입장
/app/api/chats/leave                  # 채팅방 퇴장
```

### 6.3 메시지 포맷

#### 메시지 전송 (Client → Server)
```json
{
  "type": "CHAT",
  "roomId": 1,
  "senderId": "user123",
  "message": "안녕하세요!"
}
```

#### 메시지 수신 (Server → Client)
```json
{
  "id": "507f1f77bcf86cd799439011",
  "type": "CHAT",
  "roomId": 1,
  "senderId": "user123",
  "message": "안녕하세요!",
  "sendAt": "2025-10-11T10:30:00Z"
}
```

#### 메시지 타입
- `ENTER`: 사용자 입장
- `CHAT`: 일반 채팅 메시지
- `LEAVE`: 사용자 퇴장


## 7. 프로젝트 구조

### 주요 디렉토리 설명

| 디렉토리 | 역할 | 주요 기능 |
|---------|------|----------|
| **advice/** | 전역 예외 처리 | `@RestControllerAdvice`로 모든 컨트롤러 예외 통합 핸들링 |
| **common/** | 응답 표준화 | 통일된 API 응답 구조 제공 (`CommonResult`, `SingleResult`, `PageResponse`) |
| **config/** | 인프라 설정 | WebSocket, RabbitMQ, MongoDB, JPA Auditing 등 외부 서비스 연동 설정 |
| **controller/** | REST API & WebSocket | REST 엔드포인트 및 STOMP 메시지 핸들러 정의, Swagger 문서화 |
| **exception/** | 커스텀 예외 | 도메인별 예외 클래스 (404, 409, 400) |
| **model/entity/** | 도메인 모델 | JPA 엔티티 (PostgreSQL), MongoDB 문서, 복합키 정의 |
| **model/** (DTO) | 데이터 전송 | 요청/응답 DTO 및 메시징 메시지 |
| **repository/** | 데이터 접근 | Spring Data JPA 리포지토리 (PostgreSQL), MongoDB 리포지토리 |
| **service/** | 비즈니스 로직 | 트랜잭션 관리, 권한 검증, 메시지 처리, 읽기/쓰기 분리 |
| **utils/** | 유틸리티 | 알림 발행, 공통 헬퍼 함수 |

### 구조

```
rebook-chat-service/
├── src/main/java/com/example/rebookchatservice/
│   ├── advice/                    # 전역 예외 처리
│   ├── common/                    # 공통 응답 모델
│   ├── config/                    # WebSocket, RabbitMQ, Swagger 설정
│   ├── controller/                # REST & WebSocket 컨트롤러
│   ├── exception/                 # 커스텀 예외
│   ├── model/
│   │   ├── entity/               # JPA 엔티티 & MongoDB 문서
│   │   └── (요청/응답 DTO)
│   ├── repository/                # 데이터 접근 레이어
│   ├── service/                   # 비즈니스 로직 (읽기/쓰기 분리)
│   └── utils/                     # 알림 발행 유틸리티
│
├── src/main/resources/
│   ├── application.yaml           # 기본 설정
│   ├── application-dev.yaml       # 개발 환경
│   └── application-prod.yaml      # 운영 환경
│
├── build.gradle
├── Dockerfile
└── README.md
```