# Rebook Chat Service

[![Java 17](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.13-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-blue.svg)](https://gradle.org/)

중고 도서 거래 플랫폼 **Rebook**의 실시간 채팅 마이크로서비스입니다. WebSocket/STOMP 기반으로 1:1 채팅방 관리, 메시지 송수신, 읽음 상태 추적을 제공합니다.

## 아키텍처

- 채팅흐름
![채팅](https://diagrams-noaahh.s3.ap-northeast-2.amazonaws.com/chat.png)

## 기능

### 메시지 타입

| 타입 | 설명 | 트리거 |
|------|------|--------|
| ENTER | 채팅방 입장 알림 | 사용자가 채팅방에 입장할 때 |
| CHAT | 일반 메시지 | 사용자가 메시지를 전송할 때 |
| LEAVE | 채팅방 퇴장 알림 | 사용자가 채팅방에서 나갈 때 |

### 주요 기능

- **1:1 채팅방**: 거래(tradingId) 기반으로 두 사용자 간 채팅방 생성, 동일 사용자 쌍은 중복 생성 불가
- **실시간 메시징**: WebSocket/STOMP 프로토콜로 실시간 메시지 송수신
- **읽음 상태 추적**: 채팅방별 사용자의 마지막 읽은 시간 기록, 안읽은 메시지 카운트 제공
- **메시지 이력 조회**: 페이지네이션 기반으로 과거 메시지 조회

---

## 기술 스택

### Language & Framework
- **Java 17**, **Spring Boot 3.3.13**, **Spring Cloud 2023.0.5**

### Database
- **PostgreSQL** — 채팅방, 읽음 상태 (JPA)
- **MongoDB** — 채팅 메시지 (Spring Data MongoDB)
- **Redis** — 세션 및 캐시

### Messaging
- **WebSocket/STOMP** — 실시간 채팅 (SockJS fallback)

### Cloud & MSA
- **Netflix Eureka** — 서비스 디스커버리
- **Spring Cloud Config** — 중앙 설정 관리
- **OpenFeign** — 서비스 간 HTTP 통신 (trade-service, user-service)

### Monitoring & Docs
- **Actuator**, **Prometheus**, **Sentry**
- **SpringDoc OpenAPI** (Swagger UI)

### Build & Deploy
- **Gradle**, **Docker**

---

## API 문서

Apidog에서 확인하실 수 있습니다:

```
https://x6wq8qo61i.apidog.io/
```

### REST API

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/chats/{yourId}` | 채팅방 생성 |
| GET | `/api/chats` | 내 채팅방 목록 조회 (페이지네이션) |
| GET | `/api/chats/messages/{roomId}` | 채팅 메시지 이력 조회 (페이지네이션) |

### WebSocket (STOMP)

- **접속 엔드포인트**: `/ws-chat` (SockJS 지원)
- **구독**: `/topic/room/{roomId}`
- **발행**: `/app/...` (아래 핸들러 참고)

| @MessageMapping | 설명 |
|-----------------|------|
| `/app/api/chats/enter` | 채팅방 입장 |
| `/app/api/chats/message` | 메시지 전송 |
| `/app/api/chats/chat/exit` | 채팅방 퇴장 |

---

## 프로젝트 구조

```
src/main/java/com/example/rebookchatservice/
├── client/
│   ├── trade/           # Feign Client (trade-service)
│   └── user/            # Feign Client (user-service)
├── common/
│   ├── enums/           # MessageStatus 등 공통 열거형
│   └── exception/       # 도메인 예외
├── config/              # WebSocket, JPA 등 설정
├── domain/chat/
│   ├── controller/      # REST API + WebSocket 핸들러
│   ├── dto/
│   │   ├── request/     # ChatRoomCreateRequest, ChatMessageRequest
│   │   └── response/    # ChatRoomResponse, ChatMessageResponse
│   ├── entity/          # ChatRoom (JPA), ChatMessage (MongoDB), ChatReadStatus
│   ├── repository/      # JPA & MongoDB Repository
│   └── service/
│       ├── reader/      # 조회 로직 (CQRS 패턴)
│       └── writer/      # 명령 로직 (CQRS 패턴)
└── external/            # 외부 시스템 연동
```
