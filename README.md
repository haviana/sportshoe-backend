# SportShoe Backend

Modular monolith backend for the SportShoe MVP (Kotlin + Spring Boot).

Modules (packages under `com.sportshoe`):
- `common` — shared base entity, error handling, request logging, JPA auditing config.
- `product` — Category / Product / Offer domain, read endpoints.
- `profile` — UserProfile capture (structured answers + optional free text). No recommendation logic yet.
- `recommendation` — output data shapes only. Scoring/ranking/explanation logic is a future increment, as is any AI integration.

## Prerequisites
- JDK 21
- Docker (for local Postgres and for Testcontainers-based tests)

## Running locally
```bash
docker compose up -d
./gradlew bootRun
```
The app starts on `http://localhost:8080`. Health check: `GET /actuator/health`.

Database connection defaults (overridable via `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` env vars) match `docker-compose.yml`.

## Running tests
```bash
./gradlew test
```
Tests spin up a disposable PostgreSQL container via Testcontainers — Docker must be running. No manual `docker compose up` is required for tests.

## API surface (current)
- `GET /actuator/health` — liveness/readiness.
- `GET /api/categories` — list product categories.
- `GET /api/products` / `GET /api/products?categoryCode=...` — list products (with offers).
- `GET /api/products/{id}` — get a single product.
- `POST /api/profiles` — persist a user profile (structured attributes + optional free text).
- `GET /api/profiles/{id}` — fetch a stored user profile.

## Database migrations
Managed by Flyway, scripts in `src/main/resources/db/migration`. Schema starts at `V1__init_schema.sql`.
