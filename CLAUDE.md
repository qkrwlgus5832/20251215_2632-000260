# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Start infrastructure (Kafka, Zookeeper, Kafka-UI)
cd subprojects/infra && docker compose up

# Run the application (pre-built JAR)
java -jar jar/notification-app.jar

# Build all modules
./gradlew build

# Run all tests
./gradlew test

# Run tests for a specific module
./gradlew :subprojects:ui:api:test
./gradlew :subprojects:application:service:test
./gradlew :subprojects:domain:test

# Run a single test class
./gradlew :subprojects:ui:api:test --tests "com.example.notification.controller.NotificationControllerIntegrationTest"
```

**Dev endpoints:**
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- H2 Console: `http://localhost:8080/h2-console`
- Kafka UI: `http://localhost:6067`

## Architecture

Multi-module Gradle project (Kotlin DSL). Modules follow a layered dependency:

```
ui:api
  → application:service, application:scheduler, application:notification-kafka-consumer
      → domain, infra, client:notification-sender
```

**Module responsibilities:**
- `ui:api` — REST controllers, request/response DTOs, `NotificationEventFactory`
- `application:service` — Core business logic: `NotificationLogService`, `NotificationPublisher`, `NotificationServerSender`
- `application:notification-kafka-consumer` — Kafka consumer that processes and delivers notifications
- `application:scheduler` — Two cron jobs: reserved notifications (every minute) and failed retry (every 10 min)
- `domain` — JPA entities (`NotificationLog`), enums, QueryDSL repositories
- `infra` — Kafka producer (`KafkaMessagePublisher`)
- `client:notification-sender` — WebClient-based HTTP senders for Email, SMS, KakaoTalk

## Key Flows

**Immediate notification:** `POST /notifications` (no reserveTime) → save as `PENDING` + publish `NotificationEvent` to Kafka → consumer sends via external sender → update to `SUCCESS`/`FAIL`

**Scheduled notification:** `POST /notifications` (with reserveTime) → save as `RESERVED` → `ReservedNotificationScheduler` publishes to Kafka when `sendAt <= now` → same consumer flow

**Retry flow:** `FailedNotificationRetryScheduler` (every 10 min) re-publishes `FAIL` records with `retryCount < 10` to Kafka using pessimistic write lock to prevent double-processing

## Domain Model

`NotificationLog` is the central entity. Status transitions: `PENDING` → `SUCCESS` | `FAIL`; `RESERVED` → `PENDING` (via scheduler); `FAIL` → `PENDING` (via retry scheduler); after 10 retries a `FAIL` is effectively `DEAD` (no further retries).

Idempotency: `eventId` (UUID) has a `UNIQUE` DB constraint. The consumer calls `updateNotification(event)` in a new transaction before processing to guard against duplicate delivery.

Kafka partition key is `channel:target`, guaranteeing ordering per user-channel pair. Consumer concurrency is 3 (matches topic partition count).

## Tech Stack

- Spring Boot 3.5.5, Kotlin 1.9.25, Java 17
- Spring Data JPA + QueryDSL 5.0.0 (Jakarta) for complex queries
- Spring Kafka; Spring WebFlux (WebClient for external HTTP calls)
- H2 in-memory DB in `MySQL` compatibility mode (dev and test)
- Tests: JUnit 5 + Mockk + SpringMockk
