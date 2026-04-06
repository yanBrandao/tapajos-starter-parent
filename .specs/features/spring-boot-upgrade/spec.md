# Spring Boot Upgrade Specification

## Problem Statement

The project runs on Spring Boot 2.3.3 (Aug 2020), which is EOL with no security patches. All core dependencies (Kotlin 1.4, Java 11, Spring Cloud Hoxton, Springfox) are outdated or abandoned. This blocks adoption of modern libraries and exposes downstream consumers to known vulnerabilities.

## Goals

- [ ] All modules compile and install successfully on Spring Boot 4.0.x
- [ ] Java baseline upgraded to 17+
- [ ] Kotlin upgraded to 2.2+
- [ ] Springfox replaced with springdoc-openapi (OpenAPI 3)
- [ ] All managed dependency versions updated to current stable releases

## Out of Scope

| Feature | Reason |
| --- | --- |
| AWS SDK v2 migration | Separate initiative, large scope |
| OpenTelemetry tracing | Separate initiative, replaces Jaeger |
| Adding test coverage | Separate milestone (but basic smoke tests added during upgrade) |
| New starter modules | No feature additions during upgrade |
| Sonatype Central Portal account setup / token creation | Manual step, done by maintainer outside this workflow |

---

## User Stories

### P1: Upgrade core platform versions - MVP

**User Story**: As a downstream service developer, I want the starters to run on Spring Boot 4.0.x so that I can use current framework features and receive security patches.

**Why P1**: Without this, no other upgrade work is possible.

**Acceptance Criteria**:

1. WHEN building root POM THEN `mvn clean install` SHALL succeed with Java 17+ and Spring Boot 4.0.x parent
2. WHEN a downstream project inherits from tapajos-starter-parent THEN it SHALL resolve all dependencies without conflicts
3. WHEN compiling Kotlin sources THEN Kotlin 2.2+ compiler SHALL produce no errors

**Independent Test**: Run `mvn clean install` from root -- all modules build green.

---

### P1: Replace Springfox with springdoc-openapi - MVP

**User Story**: As a downstream service developer, I want API documentation to work with Spring Boot 4.0.x so that I can expose Swagger UI for my REST endpoints.

**Why P1**: Springfox is abandoned and incompatible with Spring Boot 3+. No swagger = no API docs.

**Acceptance Criteria**:

1. WHEN a consumer adds `tapajos-starter-web` dependency THEN springdoc-openapi SHALL auto-configure OpenAPI 3 documentation
2. WHEN a class or method is annotated with an exclusion annotation THEN it SHALL be excluded from the API docs
3. WHEN Maven resource filtering runs THEN API title, description, and version SHALL be populated from the consumer's POM

**Independent Test**: Add starter-web to a test project, start it, verify `/swagger-ui.html` or `/swagger-ui/index.html` loads with correct metadata.

---

### P1: Upgrade Spring Cloud - MVP

**User Story**: As a downstream service developer, I want Consul config integration to work with Spring Boot 4.0.x.

**Why P1**: Web starter depends on spring-cloud-consul-config; must be compatible.

**Acceptance Criteria**:

1. WHEN `tapajos-starter-web` is included THEN Spring Cloud Consul config dependency SHALL resolve correctly
2. WHEN Spring Cloud BOM is imported THEN version SHALL be 2025.1.x (compatible with Spring Boot 4.0.x)

**Independent Test**: `mvn dependency:tree -pl tapajos-starters/tapajos-starter-web` shows correct Spring Cloud versions.

---

### P2: Update all managed dependency versions

**User Story**: As a downstream service developer, I want all managed dependency versions to be current so that I don't inherit known CVEs.

**Why P2**: Functional upgrade works without this, but security/compatibility improves.

**Acceptance Criteria**:

1. WHEN checking managed dependencies THEN all versions SHALL be current stable releases (no EOL versions)
2. WHEN a downstream project uses PostgreSQL, Liquibase, or Micrometer THEN managed versions SHALL be compatible with Spring Boot 4.0.x

**Independent Test**: `mvn versions:display-dependency-updates` shows no critical outdated dependencies.

---

### P2: Add proper auto-configuration registration

**User Story**: As a downstream service developer, I want starters to auto-configure without requiring me to add component-scan annotations.

**Why P2**: Existing behavior works via component scanning, but proper registration is the Spring Boot standard.

**Acceptance Criteria**:

1. WHEN `tapajos-starter-swagger` is on the classpath THEN OpenAPI configuration SHALL be auto-detected without consumer component scanning
2. WHEN checking META-INF THEN `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` SHALL exist and list auto-configuration classes

**Independent Test**: Consumer project with no @ComponentScan for starter packages still gets Swagger UI.

---

### P1: Publish to Maven Central Repository - MVP

**User Story**: As a downstream service developer, I want the starters published to Maven Central so that I can consume them without a private Nexus registry.

**Why P1**: Current publishing targets localhost Nexus which is not accessible to external consumers. Maven Central is the standard public distribution channel.

**Acceptance Criteria**:

1. WHEN running `mvn deploy` THEN artifacts SHALL be uploaded to Sonatype Central Portal (not legacy OSSRH)
2. WHEN checking published artifacts THEN they SHALL include sources JAR, javadoc JAR, and GPG signatures
3. WHEN the POM is validated by Central Portal THEN it SHALL pass all requirements (SCM, license, developer info, description present)
4. WHEN artifacts are published THEN group ID `io.github.yanbrandao` SHALL resolve on Maven Central

**Independent Test**: Run `mvn deploy` and verify artifacts appear on https://central.sonatype.com/

---

## Edge Cases

- WHEN a downstream project still uses javax.* imports THEN compilation SHALL fail clearly (not at runtime)
- WHEN jackson-module-kotlin version conflicts with Spring Boot BOM THEN the BOM version SHALL win (no hardcoded override)
- WHEN Guava is no longer on the classpath THEN swagger module SHALL not reference Guava classes

---

## Requirement Traceability

| Requirement ID | Story | Phase | Status |
| --- | --- | --- | --- |
| UPGRADE-01 | P1: Upgrade core platform | Tasks | Pending |
| UPGRADE-02 | P1: Replace Springfox | Tasks | Pending |
| UPGRADE-03 | P1: Upgrade Spring Cloud | Tasks | Pending |
| UPGRADE-04 | P2: Update managed deps | Tasks | Pending |
| UPGRADE-05 | P2: Auto-configuration registration | Tasks | Pending |
| UPGRADE-06 | P1: Publish to Maven Central | Tasks | Pending |

**Coverage:** 6 total, 6 mapped to tasks, 0 unmapped

---

## Success Criteria

- [ ] `mvn clean install` succeeds on all modules with Java 17+ and Spring Boot 4.0.x
- [ ] No javax.* imports remain in any source file
- [ ] No Springfox references remain in any POM or source file
- [ ] Dependency tree shows no Spring Boot 2.x or 3.x transitive dependencies
