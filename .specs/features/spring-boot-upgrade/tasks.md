# Spring Boot Upgrade Tasks

**Spec**: `.specs/features/spring-boot-upgrade/spec.md`
**Status**: Draft

---

## Execution Plan

### Phase 1: Root POM Foundation (Sequential)

Upgrade the root POM first -- everything else inherits from it.

```
T1 → T2
```

### Phase 2: Module Upgrades (Parallel OK)

Once root POM is upgraded, modules can be updated in parallel (bottom-up by dependency chain).

```
     ┌→ T3 (commons) ─┐
T2 ──┤                 ├──→ T5 (swagger, depends on T3)
     └→ T4 (logging)   │
                        └──→ T6 (web, depends on T5)
```

### Phase 3: Finalization (Sequential)

Wire up auto-configuration, clean up starters-parent, verify full build.

```
T6 → T7 → T8 → T9
```

### Phase 4: Maven Central Publishing (Sequential)

Replace localhost Nexus with Sonatype Central Portal. Add required plugins and POM metadata.

```
T9 → T10 → T11 → T12
```

---

## Task Breakdown

### T1: Upgrade root POM platform versions

**What**: Update spring-boot-starter-parent to 4.0.5, Java to 17, Kotlin to 2.2.0, Spring Cloud to 2025.1.0
**Where**: `pom.xml` (root)
**Depends on**: None
**Reuses**: N/A
**Requirement**: UPGRADE-01

**Done when**:

- [ ] `<parent>` spring-boot-starter-parent version is 4.0.5
- [ ] `<java.version>` is 17
- [ ] `<kotlin.version>` is 2.2.0
- [ ] `<spring-cloud.version>` is 2025.1.0
- [ ] Kotlin maven plugin dependencies use `${kotlin.version}`

**Tests**: none
**Gate**: quick (`mvn validate` on root)

---

### T2: Update all managed dependency versions in root POM

**What**: Update all `<properties>` versions: Jaeger, Micrometer, Logback-contrib, Logstash-encoder, PostgreSQL, Liquibase, JaCoCo, Mockito-Kotlin. Remove hardcoded versions where Spring Boot BOM manages them. Switch mockito-kotlin GAV from `com.nhaarman.mockitokotlin2` to `org.mockito.kotlin`.
**Where**: `pom.xml` (root), `<dependencyManagement>` and `<properties>`
**Depends on**: T1
**Reuses**: N/A
**Requirement**: UPGRADE-04

**Done when**:

- [ ] All property versions updated to current stable releases
- [ ] mockito-kotlin GAV changed to `org.mockito.kotlin:mockito-kotlin:5.x`
- [ ] Micrometer version removed (managed by Spring Boot BOM)
- [ ] PostgreSQL version removed if managed by Spring Boot BOM
- [ ] `mvn validate` succeeds

**Tests**: none
**Gate**: quick (`mvn validate`)

---

### T3: Upgrade tapajos-starter-commons module [P]

**What**: Update JacksonExtension to use modern jackson-module-kotlin API (replace deprecated `KotlinModule()` constructor). Remove hardcoded jackson-module-kotlin version from POM (let BOM manage it). Ensure Kotlin 2.2 compilation. Update kotlin-maven-plugin `jvmTarget` to 17.
**Where**: `tapajos-starters/tapajos-starter-commons/pom.xml`, `tapajos-starters/tapajos-starter-commons/src/main/kotlin/tapajos/webservice/starter/commons/extension/JacksonExtension.kt`
**Depends on**: T2
**Reuses**: Existing JacksonExtension patterns
**Requirement**: UPGRADE-01

**Done when**:

- [ ] `jackson-module-kotlin` version removed from commons POM (BOM-managed)
- [ ] `KotlinModule()` replaced with `jacksonObjectMapper()` or `KotlinModule.Builder().build()`
- [ ] `jvmTarget` changed to 17
- [ ] `mvn clean install -pl tapajos-starters/tapajos-starter-commons` succeeds
- [ ] Gate check passes: `mvn clean install -pl tapajos-starters/tapajos-starter-commons`

**Tests**: none (no existing tests; annotation + extension are low-risk pure code)
**Gate**: build

---

### T4: Upgrade tapajos-starter-logging module [P]

**What**: Verify logback-contrib and logstash-logback-encoder versions are compatible with Spring Boot 4.0.x. Update versions in root POM if needed. Check for jakarta.* namespace issues in JAXB annotations dependency.
**Where**: `tapajos-starters/tapajos-starter-logging/pom.xml`, root `pom.xml` (if version changes needed)
**Depends on**: T2
**Reuses**: N/A
**Requirement**: UPGRADE-04

**Done when**:

- [ ] `jackson-module-jaxb-annotations` replaced with `jackson-module-jakarta-xmlbind-annotations` if needed
- [ ] logback-contrib and logstash versions compatible with Spring Boot 4.0.x
- [ ] `mvn clean install -pl tapajos-starters/tapajos-starter-logging` succeeds
- [ ] Gate check passes: `mvn clean install -pl tapajos-starters/tapajos-starter-logging`

**Tests**: none
**Gate**: build

---

### T5: Replace Springfox with springdoc-openapi in swagger module

**What**: Remove Springfox dependencies. Add `springdoc-openapi-starter-webmvc-ui`. Rewrite `SwaggerAutoConfiguration` to use `GroupedOpenApi` instead of `Docket`. Replace `@EnableSwagger2` with springdoc configuration. Update `@SwaggerIgnore` handling to use springdoc's exclusion mechanism. Keep Maven resource filtering for API metadata.
**Where**: `tapajos-starters/tapajos-starter-swagger/pom.xml`, `tapajos-starters/tapajos-starter-swagger/src/main/kotlin/tapajos/webservice/starter/swagger/configuration/SwaggerAutoConfiguration.kt`, `tapajos-starters/tapajos-starter-swagger/src/main/resources/application.properties`
**Depends on**: T3 (needs commons with SwaggerIgnore annotation)
**Reuses**: Existing application.properties resource filtering pattern
**Requirement**: UPGRADE-02

**Done when**:

- [ ] No Springfox dependencies in swagger POM
- [ ] `springdoc-openapi-starter-webmvc-ui` dependency added
- [ ] `SwaggerAutoConfiguration` rewritten for springdoc (GroupedOpenApi, OpenApiCustomizer or equivalent)
- [ ] No `com.google.common.base.Predicates` import
- [ ] No `@EnableSwagger2` annotation
- [ ] @SwaggerIgnore annotation still functional (exclusion mechanism updated)
- [ ] `application.properties` still uses Maven resource filtering
- [ ] `mvn clean install -pl tapajos-starters/tapajos-starter-swagger` succeeds
- [ ] Gate check passes: `mvn clean install -pl tapajos-starters/tapajos-starter-swagger`

**Tests**: none (integration test would need a running Spring context; deferred to test milestone)
**Gate**: build

---

### T6: Upgrade tapajos-starter-web module

**What**: Update Spring Cloud Consul dependency. Verify spring-boot-starter-web, spring-boot-starter-actuator, spring-boot-starter-test resolve correctly. Update dependency on tapajos-starter-swagger.
**Where**: `tapajos-starters/tapajos-starter-web/pom.xml`
**Depends on**: T5 (needs swagger module updated)
**Reuses**: N/A
**Requirement**: UPGRADE-03

**Done when**:

- [ ] Spring Cloud Consul dependency resolves with 2025.1.x BOM
- [ ] No javax.* transitive dependencies in dependency tree
- [ ] `mvn clean install -pl tapajos-starters/tapajos-starter-web` succeeds
- [ ] Gate check passes: `mvn clean install -pl tapajos-starters/tapajos-starter-web`

**Tests**: none
**Gate**: build

---

### T7: Add auto-configuration registration

**What**: Create `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` in the swagger module listing `SwaggerAutoConfiguration`. Evaluate if commons or logging need auto-configuration registration.
**Where**: `tapajos-starters/tapajos-starter-swagger/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
**Depends on**: T6
**Reuses**: Spring Boot 4.x auto-configuration registration pattern
**Requirement**: UPGRADE-05

**Done when**:

- [ ] `AutoConfiguration.imports` file exists with correct class listing
- [ ] `mvn clean install -pl tapajos-starters/tapajos-starter-swagger` succeeds

**Tests**: none
**Gate**: build

---

### T8: Clean up tapajos-starters aggregator and starters-parent POMs

**What**: Update version references in aggregator POM. Clean up or remove the commented-out `tapajos-starters-parent` POM content. Update aggregator's `<dependencyManagement>` versions.
**Where**: `tapajos-starters/pom.xml`, `tapajos-starters/tapajos-starters-parent/pom.xml`
**Depends on**: T7
**Reuses**: N/A
**Requirement**: UPGRADE-01

**Done when**:

- [ ] Aggregator POM dependency management versions consistent
- [ ] starters-parent POM cleaned up (uncommented and updated, or removed if not needed)
- [ ] `mvn clean install` from root succeeds

**Tests**: none
**Gate**: build (`mvn clean install` from root)

---

### T9: Full build verification and dependency tree audit

**What**: Run `mvn clean install` from root. Run `mvn dependency:tree` on all modules. Verify no Spring Boot 2.x/3.x, no javax.*, no Springfox artifacts in dependency tree. Verify no Guava transitive dependency from Springfox.
**Where**: All modules
**Depends on**: T8
**Reuses**: N/A
**Requirement**: UPGRADE-01, UPGRADE-02, UPGRADE-03, UPGRADE-04, UPGRADE-05

**Done when**:

- [ ] `mvn clean install` succeeds from root (all modules green)
- [ ] `mvn dependency:tree` shows no Spring Boot 2.x or 3.x artifacts
- [ ] No `javax.*` in any source file (grep confirms)
- [ ] No `springfox` in any POM or source file
- [ ] No Guava transitive dependency from starters
- [ ] Gate check passes: `mvn clean install`

**Tests**: none (verification task)
**Gate**: build

---

### T10: Replace Nexus publishing with Sonatype Central Portal plugin

**What**: Remove `nexus-staging-maven-plugin` and localhost Nexus `<distributionManagement>`. Add `central-publishing-maven-plugin` (org.sonatype.central) with `publishingServerId=central`, `autoPublish=false` (manual approval on first publish). Remove `maven-deploy-plugin` skip configuration (Central plugin replaces both).
**Where**: `pom.xml` (root)
**Depends on**: T9
**Reuses**: N/A
**Requirement**: UPGRADE-06

**Done when**:

- [ ] `nexus-staging-maven-plugin` removed from root POM
- [ ] Localhost Nexus `<distributionManagement>` removed
- [ ] `central-publishing-maven-plugin` added with `extensions=true`, `publishingServerId=central`
- [ ] `maven-deploy-plugin` skip configuration removed
- [ ] `mvn validate` succeeds

**Tests**: none
**Gate**: quick (`mvn validate`)

---

### T11: Add Maven Central POM requirements (GPG, sources, javadoc)

**What**: Add `maven-gpg-plugin` for artifact signing. Add `maven-javadoc-plugin` (configured for Kotlin/no-source-java to produce empty javadoc JAR). Ensure `maven-source-plugin` is already active (it is). Verify POM has all Central Portal required metadata: `<name>`, `<description>`, `<url>`, `<licenses>`, `<developers>`, `<scm>` with `<url>`, `<connection>`, and `<developerConnection>`.
**Where**: `pom.xml` (root)
**Depends on**: T10
**Reuses**: Existing `maven-source-plugin` configuration
**Requirement**: UPGRADE-06

**Done when**:

- [ ] `maven-gpg-plugin` configured in root POM (sign during `verify` phase)
- [ ] `maven-javadoc-plugin` configured (produces JAR even for Kotlin-only sources)
- [ ] `<scm>` has `<developerConnection>` tag added
- [ ] All Central Portal metadata requirements present (name, description, url, license, developers, scm)
- [ ] `mvn validate` succeeds

**Tests**: none
**Gate**: quick (`mvn validate`)

---

### T12: Verify deploy dry-run and document publishing workflow

**What**: Run `mvn deploy -DskipPublishing=true` (or equivalent dry-run) to verify the full build + signing + bundle creation pipeline works without actually uploading. Document the publishing workflow: prerequisites (GPG key, Sonatype token in settings.xml), commands, and verification steps.
**Where**: All modules, update `README.md` or `.specs/project/STATE.md`
**Depends on**: T11
**Reuses**: N/A
**Requirement**: UPGRADE-06

**Done when**:

- [ ] `mvn install` with GPG signing produces signed artifacts in local repo
- [ ] All modules produce: JAR + sources JAR + javadoc JAR + .asc signatures
- [ ] Publishing prerequisites documented (GPG key setup, Sonatype token, settings.xml server entry)
- [ ] Deploy command documented
- [ ] Gate check passes: `mvn clean install`

**Tests**: none (verification task)
**Gate**: build

---

## Parallel Execution Map

```
Phase 1 (Sequential):
  T1 ──→ T2

Phase 2 (Parallel):
  T2 complete, then:
    ├── T3 [P] (commons)
    └── T4 [P] (logging)
  T3 complete, then:
    └── T5 (swagger)
  T5 complete, then:
    └── T6 (web)

Phase 3 (Sequential):
  T6 ──→ T7 ──→ T8 ──→ T9

Phase 4 (Sequential):
  T9 ��─→ T10 ──→ T11 ──→ T12
```

---

## Diagram-Definition Cross-Check

| Task | Depends On (body) | Diagram Shows | Status |
| --- | --- | --- | --- |
| T1 | None | Start | OK |
| T2 | T1 | T1 -> T2 | OK |
| T3 | T2 | T2 -> T3 | OK |
| T4 | T2 | T2 -> T4 | OK |
| T5 | T3 | T3 -> T5 | OK |
| T6 | T5 | T5 -> T6 | OK |
| T7 | T6 | T6 -> T7 | OK |
| T8 | T7 | T7 -> T8 | OK |
| T9 | T8 | T8 -> T9 | OK |
| T10 | T9 | T9 -> T10 | OK |
| T11 | T10 | T10 -> T11 | OK |
| T12 | T11 | T11 -> T12 | OK |

---

## Test Co-location Validation

| Task | Code Layer | Matrix Requires | Task Says | Status |
| --- | --- | --- | --- | --- |
| T1 | POM config | none | none | OK |
| T2 | POM config | none | none | OK |
| T3 | JacksonExtension | unit | none | OK (no existing tests to break; new tests deferred to test milestone) |
| T4 | POM config / logging deps | none | none | OK |
| T5 | SwaggerAutoConfiguration | integration | none | OK (needs Spring context; deferred to test milestone) |
| T6 | POM config | none | none | OK |
| T7 | META-INF resource | none | none | OK |
| T8 | POM config | none | none | OK |
| T9 | Verification only | none | none | OK |
| T10 | POM config (publishing plugin) | none | none | OK |
| T11 | POM config (GPG, javadoc, metadata) | none | none | OK |
| T12 | Verification + documentation | none | none | OK |
