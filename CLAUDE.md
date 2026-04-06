# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A multi-module Maven parent POM project providing reusable Spring Boot starter libraries for Kotlin web services. Group ID: `io.github.yanbrandao`, version `0.0.1a`. Built on Spring Boot 2.3.3, Kotlin 1.4.10, Java 11.

## Build Commands

```bash
# Build all modules
mvn clean install

# Build a specific starter module
mvn clean install -pl tapajos-starters/tapajos-starter-commons

# Run tests
mvn test

# Build with Docker (mounts local .m2 cache)
docker build -t dev .
docker run -v ~/.m2:/root/.m2 -it dev
```

## Architecture

This is a **POM-only parent project** — it does not produce a runnable application. It defines dependency versions and build configuration that downstream Kotlin/Spring Boot projects inherit.

### Module Hierarchy

```
tapajos-starter-parent (root POM)
└── tapajos-starters (aggregator POM)
    ├── tapajos-starter-commons   — Jackson extensions, shared annotations
    ├── tapajos-starter-swagger   — Swagger 2 auto-configuration (depends on commons)
    ├── tapajos-starter-web       — Spring Boot Web + Actuator + Consul + Swagger (depends on swagger)
    ├── tapajos-starter-logging   — Logstash/Logback JSON logging integration
    └── tapajos-starters-parent   — (mostly commented out) intended as a consumer-facing parent POM
```

### Key Dependency Chain

`web` → `swagger` → `commons`

### Source Layout

Kotlin sources live under `src/main/kotlin` (configured in the root POM's `<sourceDirectory>`). The base package is `tapajos.webservice.starter.*`.

### Notable Patterns

- **`@SwaggerIgnore`** annotation (in commons) — used by the swagger module's auto-configuration to exclude annotated classes/methods from the Swagger API docs.
- **`JacksonExtension`** (in commons) — singleton `ObjectMapper` with Kotlin module, Java Time support, and non-null inclusion. Provides Kotlin extension functions (`String.jsonToObject()`, `T.objectToJson()`, etc.) for JSON serialization.
- Swagger module uses `@PropertySource("classpath:application.properties")` with Maven resource filtering (`@project.name@` tokens) to auto-populate API metadata from the consuming project's POM.

### Publishing

Configured for Nexus deployment (`localhost:8081`) via `nexus-staging-maven-plugin`. The `maven-deploy-plugin` skip is set to `true` so deploys go through the Nexus staging plugin instead.
