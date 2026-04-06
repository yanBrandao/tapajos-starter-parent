# Tapajos Starter Parent

**Vision:** A collection of reusable Spring Boot starter libraries for Kotlin web services, providing opinionated defaults for web, swagger/OpenAPI, logging, and common utilities.
**For:** Kotlin/Spring Boot developers building microservices at io.github.yanbrandao
**Solves:** Eliminates boilerplate setup for new Kotlin web services by providing pre-configured starters with consistent patterns.

## Goals

- Provide plug-and-play starters that reduce new microservice setup to a single parent POM dependency
- Maintain consistent Jackson, Swagger/OpenAPI, logging, and web configurations across all downstream services
- Keep dependency versions aligned and tested together

## Tech Stack

**Core:**

- Framework: Spring Boot (currently 2.3.3.RELEASE, targeting 4.0.x)
- Language: Kotlin (currently 1.4.10, targeting 2.2+)
- Java: currently 11, targeting 17+
- Build: Maven (multi-module POM)

**Key dependencies:**

- Spring Cloud (Consul for config)
- Springfox Swagger 2.9.2 (to be replaced by springdoc-openapi)
- Jackson + Kotlin module
- Logstash/Logback JSON logging
- Micrometer + Prometheus metrics

## Scope

**v1 (current) includes:**

- Commons starter (Jackson extensions, shared annotations)
- Swagger starter (auto-configured Swagger 2 docs)
- Web starter (Spring Boot Web + Actuator + Consul + Swagger)
- Logging starter (Logstash/Logback JSON integration)

**Explicitly out of scope:**

- Runtime application code (this is a library-only project)
- Database starter modules (managed versions only, no auto-config)
- AWS SDK starter modules (managed versions only)

## Constraints

- Technical: Must maintain backward compatibility for downstream consumers during migration
- Resources: Single maintainer (Yan Tapajos)
