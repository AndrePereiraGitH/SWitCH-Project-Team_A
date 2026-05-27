# MITE LOVERS — TEAM A

# DevSecOps Process Documentation

**Project:** MiteLovers — SWitCH Dev 2025/2026

---

## Overview

This document describes the DevSecOps workflow adopted by Team A. It covers the local development scripts, static analysis tools, test coverage enforcement, the automated CI pipeline, and the Git workflow conventions. The goal is to ensure every change is built, linted, tested, and security-validated before being integrated into shared branches.

---

## Repository Structure (relevant files)

```
project-root/
├── .github/
│   └── workflows/
│       └── pr-tests.yml       # CI pipeline (GitHub Actions)
├── src/
│   └── main/java/...
├── checkstyle.xml              # Checkstyle rules
├── lint                        # Lint script (Unix)
├── lint.bat                    # Lint script (Windows)
├── run                         # Build + test script (Unix)
├── run.bat                     # Build + test script (Windows)
└── pom.xml                     # Maven build + plugins config
```

---

## Local Development Scripts

Two pairs of cross-platform scripts are provided so all developers can run the same commands regardless of their OS.

### `lint` / `lint.bat` — Static Analysis

Runs Checkstyle and PMD to enforce code style and detect code smells.

**Usage:**
```bash
# Unix / macOS / Git Bash
./lint

# Windows (Command Prompt)
lint.bat
```

**What it runs:**
```
mvn checkstyle:check pmd:check
```

| Tool | Purpose |
|---|---|
| Checkstyle | Enforces code style rules defined in `checkstyle.xml` |
| PMD | Detects bad practices and code smells |

> Both tools are configured to **warn only** in the lint script — violations are printed but do not stop the build. Hard enforcement happens in CI via `mvn clean verify`.

### Checkstyle Rules

Rules are defined in `checkstyle.xml` at the project root:

| Rule | Description |
|---|---|
| `EmptyBlock` | No empty code blocks (e.g. empty catch blocks) |
| `MethodLength` | Methods must not exceed 40 lines |
| `CyclomaticComplexity` | Method complexity must not exceed 15 branches/paths |
| `InterfaceIsType` | Interfaces must define types, not just constants |
| `MethodName` | Method names must follow lowerCamelCase (`^[a-z][a-zA-Z0-9]*$`) |
| `JavadocType` | Public classes must have a JavaDoc comment |
| `StringLiteralEquality` | Strings must be compared with `.equals()`, not `==` |
| `IllegalInstantiation` | Prevents unnecessary instantiation of `Boolean`, `Integer`, `Long`, `Double` |

### `run` / `run.bat` — Full Build & Test

Runs linting first, then a full Maven build with tests and coverage checks.

**Usage:**
```bash
# Unix / macOS / Git Bash
./run

# Windows (Command Prompt)
run.bat
```

**What it runs:**
```
lint  →  mvn clean verify
```

`mvn clean verify` triggers the full pipeline:
1. Compiles source and test code
2. Runs all unit tests via Surefire
3. Measures line coverage via JaCoCo
4. Measures mutation coverage via PITest
5. Fails the build if thresholds are not met

---

## Quality Gates

The following thresholds are enforced both locally (via `run`) and in CI (via the pipeline).

| Tool | Metric | Threshold | Scope |
|---|---|---|---|
| JaCoCo | Line coverage | ≥ 85% | All code except `**/ui/**` |
| PITest | Mutation coverage | ≥ 95% | All code except `domain.Enum.*` |

If either threshold is not met, `mvn clean verify` exits with a non-zero code and the build fails.

---

## CI Pipeline — GitHub Actions

File: `.github/workflows/pr-tests.yml`

### Trigger

The pipeline runs automatically on:

- **Pull Requests** (opened or updated) targeting `main`, `teamA_1`, or `teamA_2`
- **Pushes** to `main`, `teamA_1`, or `teamA_2`
- **Manual dispatch** (`workflow_dispatch`)

### Pipeline Overview

The pipeline is composed of five parallel and sequential jobs:

```
┌─────────────────────────────────────────────────────────┐
│                  Parallel (no dependencies)             │
│  build-and-test   static-analysis   secret-detection   dependency-scan  │
└────────────────────────┬────────────────────────────────┘
                         ↓ (all must pass)
                    pr-validation
                         ↓ (push events only)
                   publish-artifact
```

---

### Job: `build-and-test` — Coverage Enforcement (US054)

Compiles the project, runs all tests, and enforces coverage thresholds.

**Steps:**
1. Checkout code
2. Install Java 17 (Temurin, Maven cache)
3. `mvn clean verify` — compile, test, JaCoCo (≥ 85% line), PITest (≥ 95% mutation)
4. Upload `jacoco-report` artifact (`target/site/jacoco/`)
5. Upload `pitest-report` artifact (`target/pit-reports/`)

> Reports are uploaded even if the build fails (`if: always()`).

---

### Job: `static-analysis-and-security` — SAST & Style (US048, US056)

Runs static analysis and security scanning in parallel with the build job.

**Steps:**
1. Checkout code
2. Install Java 17 (Temurin, Maven cache)
3. **Checkstyle** — style violations reported, does not fail the build (`|| true`)
4. **PMD** — code smell detection reported, does not fail the build (`|| true`)
5. **Semgrep SAST scan** — scans with `p/java`, `p/owasp-top-ten`, `p/spring`; **fails the build** on `ERROR` severity findings
6. Upload `checkstyle-report` artifact (`target/checkstyle-result.xml`)
7. Upload `pmd-report` artifact (`target/pmd.xml`)
8. **Post Semgrep findings to PR** (on pull requests) — publishes a comment with a severity breakdown table (ERROR / WARNING / INFO counts) and flags if the build is blocked

> Requires `SEMGREP_APP_TOKEN` secret to be configured in the repository.

---

### Job: `secret-detection` — Secret & Credential Scanning (US049)

Scans the full Git history for leaked secrets and credentials.

**Steps:**
1. Checkout code with **full history** (`fetch-depth: 0`) so all commits are scanned
2. **Gitleaks** — scans the entire git history; **fails the build** if any secret is found

> Requires `GITHUB_TOKEN` to be available (automatically provided by GitHub Actions).

---

### Job: `dependency-scan` — CVE & Dependency Audit (US053, US056)

Generates a full dependency inventory and scans for known vulnerabilities.

**Steps:**
1. Checkout code
2. Install Java 17 (Temurin, Maven cache)
3. **`mvn dependency:tree`** — generates `target/dependency-tree.txt` with all direct and transitive dependencies
4. **OWASP Dependency-Check** — CVE scan against the NVD database; **fails the build** on any finding with CVSS ≥ 7 (HIGH or CRITICAL)
5. Upload `dependency-tree` artifact (`target/dependency-tree.txt`)
6. Upload `dependency-check-report` artifact (`target/dependency-check-report.html`)
7. **Post OWASP findings to PR** (on pull requests) — publishes a comment with a severity breakdown (CRITICAL / HIGH / MEDIUM / LOW counts) and flags if the build is blocked

> Requires `NVD_API_KEY` secret to be configured in the repository for NVD API access.

---

### Job: `pr-validation` — Aggregated Gate (US051, US052, US057)

Runs only after **all four** upstream jobs pass. Acts as the final integration gate.

**Steps:**
1. **Verify all gates passed** — prints a summary of all quality gates to the Actions log
2. **Publish quality summary to PR** (on pull requests) — posts a consolidated CI Quality Report comment with a table of all jobs, tools, and thresholds

A PR cannot be merged until this job succeeds, which requires every preceding job to have passed.

---

### Job: `publish-artifact` — Versioned Build (US050)

Runs only after `pr-validation` passes, and **only on push events** (i.e. when a PR is merged into a protected branch — not on every PR open/sync).

**Steps:**
1. Checkout code
2. Install Java 17 (Temurin, Maven cache)
3. **Set version from commit SHA** — stamps the artifact version as `1.0-<short-sha>` for traceability
4. **`mvn package -DskipTests`** — builds the JAR (tests already validated in `build-and-test`)
5. Upload versioned JAR as `app-<full-sha>` artifact, retained for **90 days**

---

### Pipeline Gate Summary

| Job | Gate Type | Tool | Threshold / Condition |
|---|---|---|---|
| `build-and-test` | **Hard gate** | JaCoCo | ≥ 85% line coverage |
| `build-and-test` | **Hard gate** | PITest | ≥ 95% mutation coverage |
| `static-analysis-and-security` | **Hard gate** | Semgrep | No ERROR severity findings |
| `static-analysis-and-security` | **Report only** | Checkstyle | Style violations logged |
| `static-analysis-and-security` | **Report only** | PMD | Code smells logged |
| `secret-detection` | **Hard gate** | Gitleaks | No secrets in full git history |
| `dependency-scan` | **Hard gate** | OWASP Dependency-Check | CVSS < 7 (no HIGH/CRITICAL CVEs) |

### Artifacts Produced

| Artifact Name | Source Job | Path | Retention |
|---|---|---|---|
| `jacoco-report` | `build-and-test` | `target/site/jacoco/` | Default |
| `pitest-report` | `build-and-test` | `target/pit-reports/` | Default |
| `checkstyle-report` | `static-analysis-and-security` | `target/checkstyle-result.xml` | Default |
| `pmd-report` | `static-analysis-and-security` | `target/pmd.xml` | Default |
| `dependency-tree` | `dependency-scan` | `target/dependency-tree.txt` | Default |
| `dependency-check-report` | `dependency-scan` | `target/dependency-check-report.html` | Default |
| `app-<sha>` | `publish-artifact` | `target/*.jar` | 90 days |

All artifacts are accessible from the **Actions → [run] → Artifacts** section on GitHub.

### Required Secrets

| Secret | Used By | Purpose |
|---|---|---|
| `SEMGREP_APP_TOKEN` | `static-analysis-and-security` | Authenticates the Semgrep SAST scan |
| `NVD_API_KEY` | `dependency-scan` | Authenticates NVD API calls for CVE data |
| `GITHUB_TOKEN` | `secret-detection`, PR comments | Provided automatically by GitHub Actions |

---

## Database & Persistence

**Related User Story:** US030 — *As a PO, I want the system to persist the information in a relational database system.*

The application uses an **H2 in-memory relational database** for development and testing. It is managed via Spring Data JPA / Hibernate and configured in `src/main/resources/application.properties`.

### Configuration

```properties
# H2 Database configuration
spring.datasource.url=jdbc:h2:mem:mitelovers
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
server.port=8081

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# H2 Console (web UI)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### Key Settings

| Property | Value | Description |
|---|---|---|
| `spring.datasource.url` | `jdbc:h2:mem:mitelovers` | In-memory H2 database named `mitelovers`. Data is lost on application restart. |
| `spring.datasource.driverClassName` | `org.h2.Driver` | H2 JDBC driver |
| `spring.datasource.username` | `sa` | Default H2 username (no password set) |
| `server.port` | `8081` | Application runs on port 8081 |
| `spring.jpa.hibernate.ddl-auto` | `update` | Hibernate automatically creates/updates the schema to match entity definitions on startup |
| `spring.jpa.show-sql` | `true` | Logs all SQL statements to the console (useful for debugging) |
| `spring.h2.console.enabled` | `true` | Enables the H2 web console |
| `spring.h2.console.path` | `/h2-console` | URL path for the H2 web console |

### H2 Web Console

While the application is running, the H2 browser console is accessible at:

```
http://localhost:8081/h2-console
```

Use the following credentials to connect:

| Field | Value |
|---|---|
| JDBC URL | `jdbc:h2:mem:mitelovers` |
| User Name | `sa` |
| Password | *(leave blank)* |

> **Note:** Because the database is in-memory (`mem:`), all data is reset every time the application restarts. This is intentional for development and testing. If persistent storage across restarts is needed in the future, the datasource URL can be changed to a file-based H2 URL (e.g. `jdbc:h2:file:./data/mitelovers`) or replaced with a production database such as PostgreSQL.

---

## Git Workflow

### Branch Strategy

The project follows a **scrum of scrums** branching model:

| Branch | Purpose |
|---|---|
| `main` | Base branch for the full project team. Receives merged, validated work. |
| `teamA_1` | Feature branch for scrum team A1. |
| `teamA_2` | Feature branch for scrum team A2. |

All PRs must target one of the branches above. Direct commits to `main` are not permitted.

### Commit Message Convention

Commits must follow the format:

```
<type>: <short description> #<issue_number>
```

**Types:**

| Type | When to use |
|---|---|
| `feat` | New feature or functionality |
| `fix` | Bug fix |
| `refactor` | Code restructuring without behaviour change |
| `docs` | Documentation only changes |
| `chore` | Build, config, or tooling changes |
| `perf` | Performance improvements or cleanup |
| `test` | Adding or updating tests |

**Examples:**
```
feat: add CI pipeline with test coverage enforcement #645 #646
feat: add command-line build, lint scripts and checkstyle config #644
docs: devSecOps documentation #647
refactor: updated pom.xml to include spring boot dependencies
perf: delete commented or duplicated methods
```

> All commits must reference at least one issue number. Multiple issues can be referenced by chaining them: `#644 #645 #646`.

### Pull Request Process

Every PR must follow the project PR template, which includes:

- **Description** — what the PR does and why
- **Related Issues** — linked with `Closes #<number>` to auto-close issues on merge
- **Changes Made** — bullet list of the main changes
- **Checklist** — code guidelines, self-review, tests, documentation
- **Notes / Known Issues** — anything reviewers should be aware of
- **Reviewer Notes** — specific areas where feedback is requested

**PR title format:**
```
<type>: <short description> (#issue1 #issue2 ...)
```

Example:
```
feat: DevSecOps setup and Sprint 5 project structure (#644 #645 #646 #647)
```

**CI must pass** before any PR can be merged. The `pr-validation` job acts as the final gate — it only succeeds when all four upstream jobs (`build-and-test`, `static-analysis-and-security`, `secret-detection`, `dependency-scan`) have passed.

---

## Developer Workflow

The expected workflow for each team member is as follows:

1. **Pick up an issue** from the sprint backlog. All commits must reference at least one issue number.
2. **Work on your feature branch** (`teamA_1` or `teamA_2`).
3. **Before committing**, run `./lint` locally to catch style violations early.
4. **Before opening a PR**, run `./run` locally to verify the build and all coverage thresholds pass.
5. **Open a Pull Request** targeting the appropriate base branch, following the PR template and title convention.
6. **CI must pass** before the PR can be merged. A failing pipeline (build error, test failure, coverage below threshold, secrets detected, or HIGH/CRITICAL CVEs) blocks integration.
7. **Review artifacts** — JaCoCo, PITest, Checkstyle, PMD, OWASP, and dependency-tree reports are all available from the Actions run page.
8. **Check PR comments** — Semgrep and OWASP results are automatically posted as PR comments with severity breakdowns. A consolidated quality summary is posted once all gates pass.

---

## Tools & Versions

| Tool | Version |
|---|---|
| Java | 17 (Temurin) |
| Spring Boot | 3.2.5 |
| Maven Surefire | 3.2.5 |
| JaCoCo | 0.8.11 |
| PITest | 1.15.8 |
| PITest JUnit5 plugin | 1.1.2 |
| Checkstyle plugin | 3.3.1 |
| PMD plugin | 3.21.2 |
| Semgrep | semgrep-action@v1 |
| Gitleaks | gitleaks-action@v2 |
| OWASP Dependency-Check | dependency-check-maven (latest) |
| JUnit Jupiter | 5.10.2 |
| Mockito | 5.10.0 |
| GitHub Actions runner | ubuntu-latest |

---