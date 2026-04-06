# Roadmap

**Current Milestone:** Spring Boot Upgrade
**Status:** Planning

---

## Milestone 1: Spring Boot Upgrade

**Goal:** Upgrade from Spring Boot 2.3.3 to 4.0.x with all modules compiling and functional
**Target:** Complete migration with all starters working on Spring Boot 4.0.x, Java 17+, Kotlin 2.2+

### Features

**Spring Boot 4.0 Migration** - IN PROGRESS

- Upgrade Java baseline from 11 to 17+
- Upgrade Kotlin from 1.4.10 to 2.2+
- Migrate javax.* to jakarta.* namespace
- Upgrade Spring Boot parent from 2.3.3 to 4.0.x
- Upgrade Spring Cloud from Hoxton to 2025.1.x
- Replace Springfox Swagger 2 with springdoc-openapi
- Update all managed dependency versions
- Migrate spring.factories to META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports

---

## Milestone 2: Modernization & Hardening

**Goal:** Leverage new Spring Boot 4.x features and improve project quality

### Features

**Version Management Modernization** - PLANNED

- Evaluate BOM-based version management
- Update Nexus publishing configuration

**Test Coverage** - PLANNED

- Add unit tests for JacksonExtension
- Add integration tests for SwaggerAutoConfiguration
- Add build CI pipeline

---

## Future Considerations

- OpenTelemetry tracing (replace Jaeger client)
- AWS SDK v2 migration (com.amazonaws -> software.amazon.awssdk)
- GraalVM native-image support
- Central Maven repository publishing
