# State

**Last Updated:** 2026-04-06
**Current Work:** Spring Boot Upgrade - Planning

---

## Recent Decisions (Last 60 days)

### AD-001: Target Spring Boot 4.0.x directly (2026-04-06)

**Decision:** Skip intermediate Spring Boot 3.x and go straight to 4.0.x
**Reason:** Spring Boot 2.x is EOL; 3.x support is ending. 4.0.x is the current stable line (4.0.5 as of March 2026).
**Trade-off:** Larger migration scope in one step, but avoids doing the work twice.
**Impact:** Requires Java 17+, Kotlin 2.2+, Jakarta EE 11, Spring Framework 7.x

### AD-002: Replace Springfox with springdoc-openapi (2026-04-06)

**Decision:** Migrate from Springfox Swagger 2.9.2 to springdoc-openapi
**Reason:** Springfox is abandoned (last release 2018), incompatible with Spring Boot 3+
**Trade-off:** Breaking API change for downstream consumers using SwaggerAutoConfiguration
**Impact:** swagger module needs full rewrite; @SwaggerIgnore annotation behavior needs equivalent in springdoc

---

## Active Blockers

_None_

---

## Lessons Learned

_None yet_

---

## Quick Tasks Completed

| #   | Description | Date | Commit | Status |
| --- | ----------- | ---- | ------ | ------ |

---

## Deferred Ideas

- [ ] OpenTelemetry tracing to replace Jaeger client -- Captured during: Spring Boot Upgrade planning
- [ ] AWS SDK v2 migration -- Captured during: Spring Boot Upgrade planning

---

## Todos

- [ ] Verify downstream consumer impact of the upgrade

---

## Preferences

**Model Guidance Shown:** never
