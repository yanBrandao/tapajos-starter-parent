# External Integrations

## Service Discovery & Configuration

**Service:** HashiCorp Consul
**Purpose:** Service discovery and distributed configuration for downstream consumers
**Implementation:** `spring-cloud-consul-config` dependency in `tapajos-starter-web`
**Configuration:** Managed by Spring Cloud; consumer provides Consul connection details

## API Documentation

**Service:** Springfox Swagger 2
**Purpose:** Auto-generated REST API documentation and Swagger UI
**Implementation:** `tapajos-starter-swagger/src/main/kotlin/.../SwaggerAutoConfiguration.kt`
**Configuration:** Maven resource filtering populates API title, description, version from consumer POM

## Metrics & Monitoring

**Service:** Micrometer + Prometheus
**Purpose:** Application metrics collection and export
**Implementation:** Managed dependency versions in root POM; consumer activates via spring-boot-starter-actuator
**Configuration:** Consumer configures Prometheus scraping endpoint

## Tracing

**Service:** Jaeger
**Purpose:** Distributed tracing
**Implementation:** Managed dependency version (jaeger-client 1.3.2) in root POM
**Configuration:** Consumer configures Jaeger agent connection

## Logging Pipeline

**Service:** Logstash (via Logback encoder)
**Purpose:** Structured JSON log output for log aggregation systems
**Implementation:** `tapajos-starter-logging` bundles logback-contrib + logstash-logback-encoder
**Configuration:** Consumer provides logback.xml with Logstash appender configuration

## Publishing

**Service:** Sonatype Nexus (localhost:8081)
**Purpose:** Artifact repository for publishing starter JARs
**Implementation:** nexus-staging-maven-plugin 1.6.7 in root POM
**Configuration:** `distributionManagement` in root POM points to localhost Nexus
