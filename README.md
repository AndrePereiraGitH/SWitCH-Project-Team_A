# MITE LOVERS — TEAM A

# DevSecOps Process Documentation

**Project:** MiteLovers — SWitCH Dev 2025/2026

---

## Overview

This document describes the DevSecOps workflow adopted by Team A. It covers the local development scripts, static analysis tools, test coverage enforcement, the automated CI pipeline, container build/runtime hardening, and the Git workflow conventions. The goal is to ensure every change is built, linted, tested, security-validated, and packaged into hardened, scanned container images before being integrated into shared branches or deployed.

> Sprint 7 extends the Sprint 5/6 pipeline with container image scanning, registry publishing, and runtime hardening (US072–US076). See [Sprint 7 DevSecOps Traceability](#sprint-7-devsecops-traceability) at the end of this document for a full mapping of user stories to where each is addressed.

---

## Repository Structure (relevant files)

```
project-root/
├── .github/
│   └── workflows/
│       └── pr_tests.yml        # CI pipeline (GitHub Actions)
├── src/
│   └── main/java/...
├── frontend/
│   ├── Dockerfile               # Frontend image: Node build → nginx-unprivileged runtime
│   ├── .dockerignore
│   └── nginx.conf
├── Dockerfile                   # Backend image: Maven build → Temurin JRE runtime
├── .dockerignore
├── docker-compose.yml           # Hardened runtime config for both services (US074, US075)
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

> PITest spawns a child JVM and requires sufficient available memory. If running locally in WSL, ensure at least 8 GB is allocated in `~/.wslconfig`. To skip PITest locally and only verify JaCoCo coverage, run `mvn clean verify -DskipPitest` (requires `<skip>${skipPitest}</skip>` in the PITest configuration).

---

## Quality Gates

The following thresholds are enforced both locally (via `run`) and in CI (via the pipeline).

| Tool | Metric | Threshold | Scope |
|---|---|---|---|
| JaCoCo | Line coverage | ≥ 85% | All code except `**/ui/**` |
| PITest | Mutation coverage | ≥ 95% | All code except `domain.Enum.*`, `ListingDataInitializer`, `dto.*.request.*`, `dto.*.response.*` |

Thresholds are enforced at **bundle level** — the project as a whole must meet the threshold, not each individual class.

If either threshold is not met, `mvn clean verify` exits with a non-zero code and the build fails.

---

## CI Pipeline — GitHub Actions

File: `.github/workflows/pr_tests.yml`

### Trigger

The pipeline runs automatically on:

- **Pull Requests** (opened or updated) targeting `main`, `teamA_1`, `teamA_2`, or `frontend`
- **Pushes** to `main`, `teamA_1`, `teamA_2`, or `frontend`

> The `frontend` branch was added in Sprint 7 alongside the React Web UI work; it is gated by the same quality, security, and container checks as the backend branches.

### Permissions

The workflow declares explicit permissions at both the workflow level and per-job level following the principle of least privilege:

| Permission | Scope |
|---|---|
| `contents: read` | All jobs |
| `actions: write` | Jobs that upload artifacts |
| `issues: write` | Jobs that post PR comments |
| `pull-requests: write` | Jobs that post PR comments |

Artifact upload steps are guarded with `github.event.pull_request.head.repo.full_name == github.repository` to prevent 403 failures when running on fork PRs.

> **Note:** `container-image-scan` posts a PR comment via `github.rest.issues.createComment` but its permissions block only grants `pull-requests: write`, not `issues: write` (unlike `static-analysis-and-security` and `dependency-scan`, which grant both for the same kind of call). Worth double-checking that comment posting actually succeeds for this job; if it silently fails, add `issues: write` to match the other jobs.

### Pipeline Overview

```
┌───────────────────────────────────────────────────────────────────────────┐
│                         Parallel (no dependencies)                        │
│  build-and-test   static-analysis-and-security   secret-detection         │
│  dependency-scan  container-image-scan                                    │
└───────────────────────────────────┬───────────────────────────────────────┘
                                     │
                                     ↓ (container-image-scan only)
                              push-to-registry
                          (main branch / PRs into main only —
                           skipped on teamA_1, teamA_2, frontend)
                                     │
                                     ↓ (5 jobs must succeed; push-to-registry
                                        may be 'success' OR 'skipped')
                               pr-validation
                                     │
                                     ↓ (no event/branch filter — runs on PR and push alike)
                             publish-artifact
```

> **Note:** `push-to-registry` is now restricted with `if: github.ref == 'refs/heads/main' || github.base_ref == 'main'`, so it only publishes images for `main` (direct pushes, or PRs whose base branch is `main`); it's skipped on `teamA_1`, `teamA_2`, and `frontend`. Because `pr-validation` depends on it, its `if` was changed from the default `success()` to an explicit check that requires every other job to succeed while accepting `push-to-registry` being `success` **or** `skipped` — otherwise the entire validation gate would have been skipped on every non-`main` run, since GitHub Actions treats a skipped dependency the same as a failed one under the default `success()`.

> **Note:** `publish-artifact` still has no event-type or branch filter — it builds and uploads a versioned JAR on every successful pipeline run, including PRs to `teamA_1`/`teamA_2`/`frontend`, not only on merges to `main`. If that should also be restricted to `main`, apply the same kind of guard used on `push-to-registry`.

---

### Job: `build-and-test` — Coverage Enforcement (US054)

Compiles the project, runs all tests, and enforces coverage thresholds.

**Steps:**
1. Checkout code
2. Install Java 17 (Temurin, Maven cache)
3. `mvn clean verify` — compile, test, JaCoCo (≥ 85% line), PITest (≥ 95% mutation)
4. Upload `jacoco-report` artifact (`target/site/jacoco/`)
5. Upload `pitest-report` artifact (`target/pit-reports/`)

> Reports are uploaded even if the build fails (`if: always()`). Uploads are skipped on fork PRs.

---

### Job: `static-analysis-and-security` — SAST & Style (US048, US056)

Runs static analysis and security scanning in parallel with the build job.

**Steps:**
1. Checkout code
2. Install Java 17 (Temurin, Maven cache)
3. **Checkstyle** — style violations reported, does not fail the build (`|| true`)
4. **PMD** — code smell detection reported, does not fail the build (`|| true`)
5. **Semgrep SAST scan (gate)** — installs Semgrep via pip, scans with `p/java` and `p/owasp-top-ten` (`--metrics=off` to disable telemetry); **fails the build** on any finding (`--error` flag)
6. Upload `checkstyle-report` artifact (`target/checkstyle-result.xml`)
7. Upload `pmd-report` artifact (`target/pmd.xml`)
8. **Post Semgrep findings to PR** (on pull requests) — re-runs Semgrep with `--json` to parse results, publishes a comment with a severity breakdown table (ERROR / WARNING / INFO counts)

> Requires `SEMGREP_APP_TOKEN` secret to be configured in the repository. The token authenticates against semgrep.dev to download rulesets.

> **Note:** The ruleset used for the **gate** (step 5: `p/java`, `p/owasp-top-ten`) differs from the ruleset used for the **PR comment** (step 8: `p/java`, `p/owasp-top-ten`, **and `p/spring`**). This means the comment's counts can include `p/spring` findings that did *not* actually block the build — worth aligning the two rulesets, or noting explicitly in the PR comment which ruleset gates and which is informational only.

---

### Job: `secret-detection` — Secret & Credential Scanning (US049)

Scans the full Git history for leaked secrets and credentials.

**Steps:**
1. Checkout code with **full history** (`fetch-depth: 0`) so all commits are scanned
2. **Gitleaks** — scans the entire git history; **fails the build** if any secret is found

> Requires `GITHUB_TOKEN` to be available (automatically provided by GitHub Actions).

> To verify Gitleaks locally before pushing: `gitleaks detect --source . --no-git --verbose`

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
7. **Post OWASP findings to PR** (on pull requests) — publishes a comment with a severity breakdown (CRITICAL / HIGH / MEDIUM / LOW counts)

> Requires `NVD_API_KEY` secret to be configured in the repository for NVD API access. The NVD database is cached across runs using `actions/cache@v4`.

---

### Job: `container-image-scan` — Container Vulnerability Scan (US073)

Builds both Docker images and scans them for known vulnerabilities before they are allowed into the registry.

**Steps:**
1. Checkout code
2. **Build backend image** — `docker build -t mite-lovers-backend:scan .`
3. **Build frontend image** — `docker build -t mite-lovers-frontend:scan ./frontend`
4. Download Trivy's upstream HTML report template (`aquasecurity/trivy` repo, `contrib/html.tpl`)
5. **Trivy scan — backend** — once in `table` format (for the PR comment) and once in `template`/HTML format (for the artifact); severity filtered to `CRITICAL,HIGH`
6. **Trivy scan — frontend** — same two formats as the backend
7. Upload `trivy-backend-report` artifact (`trivy-backend.txt` + `trivy-backend.html`)
8. Upload `trivy-frontend-report` artifact (`trivy-frontend.txt` + `trivy-frontend.html`)
9. **Publish Trivy findings to PR** (on pull requests) — posts a comment with the first 3000 characters of each table report and a pointer to the full HTML reports in the run's artifacts

> **Note:** Each Trivy scan step is configured with `exit-code: '0'`, meaning CRITICAL/HIGH findings are **reported but do not fail the build**. Unlike the Semgrep, Gitleaks, and OWASP jobs, this is currently a soft gate — worth confirming with the team whether US073's "automatically scanned **before acceptance**" intent requires this to become a hard gate (e.g. `exit-code: '1'`) before final delivery.

> **Note:** `aquasecurity/trivy-action` is pinned to the **`@master`** tag rather than a specific release/SHA — the one unpinned third-party action in a pipeline that otherwise enforces pinned base images (US072). Consider pinning to a tagged release for reproducibility.

---

### Job: `push-to-registry` — Publish Images to Docker Hub (US073)

Logs in to Docker Hub and pushes both images, tagged for traceability back to the source commit (US076) — **only for the `main` branch**.

**Needs:** `container-image-scan`

**Runs only if:** `github.ref == 'refs/heads/main' || github.base_ref == 'main'` — i.e. a direct push to `main`, or a pull request whose base branch is `main`. On every other branch (`teamA_1`, `teamA_2`, `frontend`) the job is skipped, and no images are pushed to Docker Hub.

**Steps:**
1. Checkout code
2. **Docker Hub login** — via `docker/login-action@v3` using the `DOCKERHUB_USERNAME` / `DOCKERHUB_TOKEN_MITELOVERS` secrets
3. **Build & push backend image** — tagged `:latest` and `:${{ github.sha }}`
4. **Build & push frontend image** — tagged `:latest` and `:${{ github.sha }}`

> Because `push-to-registry` is now conditionally skipped, `pr-validation`'s gate condition had to be adjusted accordingly — see the note below.

---

### Job: `pr-validation` — Aggregated Gate (US051, US052, US057)

Acts as the final integration gate, depending on all six upstream jobs (`build-and-test`, `static-analysis-and-security`, `secret-detection`, `dependency-scan`, `container-image-scan`, `push-to-registry`).

**Runs only if:**
```
always() &&
needs.build-and-test.result == 'success' &&
needs.static-analysis-and-security.result == 'success' &&
needs.secret-detection.result == 'success' &&
needs.dependency-scan.result == 'success' &&
needs.container-image-scan.result == 'success' &&
(needs.push-to-registry.result == 'success' || needs.push-to-registry.result == 'skipped')
```

This is an explicit condition rather than the plain `if: success()` used before. It's needed because `push-to-registry` is now intentionally skipped on `teamA_1`/`teamA_2`/`frontend` (see above), and GitHub Actions' default `success()` treats a skipped dependency the same as a failed one — so without this change, `pr-validation` (and everything that follows it) would have been skipped on every branch except `main`.

**Steps:**
1. **Verify all gates passed** — prints a summary of all quality and security gates, including the container scan, to the Actions log
2. **Publish quality summary to PR** (on pull requests) — posts a consolidated CI Quality Report comment with a table of all jobs, gates, and tools (the two Trivy rows are marked **Reported**, not **Passed**, reflecting the soft-gate note above)

A PR cannot be merged until this job succeeds: every job must have actually passed, except `push-to-registry`, which is allowed to have been skipped (on non-`main` branches) without blocking the gate.

---

### Job: `publish-artifact` — Versioned Build (US050)

Runs only after `pr-validation` passes. **It has no event-type filter**, so it currently runs on every successful run of the pipeline — both pull request and push events — not only when a PR is merged into a protected branch.

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
| `build-and-test` | **Hard gate** | JaCoCo | ≥ 85% line coverage (bundle) |
| `build-and-test` | **Hard gate** | PITest | ≥ 95% mutation coverage (bundle) |
| `static-analysis-and-security` | **Hard gate** | Semgrep | No findings (`--error` flag; rulesets: `p/java`, `p/owasp-top-ten`) |
| `static-analysis-and-security` | **Report only** | Checkstyle | Style violations logged |
| `static-analysis-and-security` | **Report only** | PMD | Code smells logged |
| `secret-detection` | **Hard gate** | Gitleaks | No secrets in full git history |
| `dependency-scan` | **Hard gate** | OWASP Dependency-Check | CVSS < 7 (no HIGH/CRITICAL CVEs) |
| `container-image-scan` | **Report only*** | Trivy (backend) | CRITICAL/HIGH findings logged (`exit-code: 0`) |
| `container-image-scan` | **Report only*** | Trivy (frontend) | CRITICAL/HIGH findings logged (`exit-code: 0`) |

*\* See the note in the `container-image-scan` job section — this is a soft gate by current configuration, even though `push-to-registry` and `pr-validation` both depend on the job completing.*

### Artifacts Produced

| Artifact Name | Source Job | Path | Retention |
|---|---|---|---|
| `jacoco-report` | `build-and-test` | `target/site/jacoco/` | Default |
| `pitest-report` | `build-and-test` | `target/pit-reports/` | Default |
| `checkstyle-report` | `static-analysis-and-security` | `target/checkstyle-result.xml` | Default |
| `pmd-report` | `static-analysis-and-security` | `target/pmd.xml` | Default |
| `dependency-tree` | `dependency-scan` | `target/dependency-tree.txt` | Default |
| `dependency-check-report` | `dependency-scan` | `target/dependency-check-report.html` | Default |
| `trivy-backend-report` | `container-image-scan` | `trivy-backend.txt`, `trivy-backend.html` | Default |
| `trivy-frontend-report` | `container-image-scan` | `trivy-frontend.txt`, `trivy-frontend.html` | Default |
| `app-<sha>` | `publish-artifact` | `target/*.jar` | 90 days |

All artifacts are accessible from the **Actions → [run] → Artifacts** section on GitHub.

### Required Secrets

| Secret | Used By | Purpose |
|---|---|---|
| `SEMGREP_APP_TOKEN` | `static-analysis-and-security` | Authenticates Semgrep against semgrep.dev to download rulesets |
| `NVD_API_KEY` | `dependency-scan` | Authenticates NVD API calls for CVE data |
| `GITHUB_TOKEN` | `secret-detection`, PR comments | Provided automatically by GitHub Actions |
| `DOCKERHUB_USERNAME` | `push-to-registry` | Docker Hub account used to publish images |
| `DOCKERHUB_TOKEN_MITELOVERS` | `push-to-registry` | Docker Hub access token (used in place of a password) |

---

## Docker Images (US072)

Both services are built as multi-stage Docker images, so build-time tooling (Maven, npm) never ships inside the final runtime image.

### Backend — `Dockerfile`

| Stage | Base Image | Purpose |
|---|---|---|
| `build` | `maven:3.9.6-eclipse-temurin-17-alpine` | Compiles the project and packages the JAR (`mvn package -DskipTests`) |
| `runtime` | `eclipse-temurin:17.0.14_7-jre-alpine` | Runs the packaged JAR — JRE only, no JDK or Maven |

Key practices:
- Both base images are **pinned to a specific version tag**, not `latest`.
- A dedicated **non-root user** (`appuser`/`appgroup`) is created, the JAR is `chown`'d to it, and the container switches to that user with `USER appuser` before `ENTRYPOINT` runs.
- Only `pom.xml` and `src/` are copied into the build stage; everything else is excluded via `.dockerignore`.
- The final image exposes only port `8081`. No secrets are hardcoded — runtime configuration (e.g. `SPRING_PROFILES_ACTIVE`) is supplied via `docker-compose.yml` environment variables.

### Frontend — `frontend/Dockerfile`

| Stage | Base Image | Purpose |
|---|---|---|
| `build` | `node:20.14.0-alpine` | Installs dependencies with `npm ci` and runs the Vite build (`npm run build`) |
| `runtime` | `nginxinc/nginx-unprivileged:1.27-alpine` | Serves the built static assets |

Key practices:
- `npm ci` (not `npm install`) installs strictly from the committed `package-lock.json`, so the build is reproducible (see also US070 below).
- `nginx-unprivileged` already runs as a non-root user by design, so no extra `USER`/`addgroup` step is needed here, unlike the backend image.
- `VITE_API_BASE_URL` is passed in as a build `ARG`/`ENV` rather than hardcoded, defaulting to `http://localhost:5173` if not supplied.
- The final image exposes only port `5173`.

### `.dockerignore`

| File | Excludes |
|---|---|
| `/.dockerignore` (backend) | `.git`, `.github`, `.idea`, `target`, `*.md`, `frontend`, `Diagrams`, `SprintReview` |
| `frontend/.dockerignore` | `.git`, `node_modules`, `dist`, `*.md` |

Excluding these paths keeps each build context small, keeps documentation/diagram/IDE files out of the images, and avoids invalidating the Docker layer cache on commits that only touch unrelated files such as `*.md`.

---

## Container Runtime Hardening & Compose Configuration (US074, US075, US076)

`docker-compose.yml` defines the runtime configuration for both services, following least-privilege and resource-bounding principles.

### Hardened runtime settings (US074)

| Setting | Backend | Frontend | Effect |
|---|---|---|---|
| `security_opt: no-new-privileges:true` | ✅ | ✅ | Prevents the container from gaining additional privileges (e.g. via setuid binaries) |
| `cap_drop: ALL` | ✅ | ✅ | Drops all Linux capabilities — neither service needs any of them |
| `read_only: true` | ✅ | — | Root filesystem mounted read-only; paired with a `tmpfs` mount at `/tmp` since the JVM still needs a writable temp directory |
| `deploy.resources.limits` | 1.0 CPU / 512M | 0.5 CPU / 128M | Bounds the blast radius of a compromised or runaway container |
| `restart: unless-stopped` | ✅ | ✅ | Restarts automatically on crash, but not after a deliberate stop |

> The frontend does not set `read_only: true`. Nginx typically needs a writable path for PID files/caches unless those are also redirected to `tmpfs`; if full read-only enforcement is wanted for the frontend too, add equivalent `tmpfs` mounts (e.g. `/var/cache/nginx`, `/var/run`) the way the backend does for `/tmp`.

### Least-privilege Compose configuration (US075)

- **Ports:** only the ports the application needs are published — `8081` (backend) and `5173` (frontend). No host volume mounts are defined.
- **Health checks:** the backend polls `/actuator/health` and the frontend polls its own root path, both every 30s; the frontend's `depends_on: backend: condition: service_healthy` means it won't start serving traffic until the backend reports healthy, not merely running.
- **Environment variables:** `SPRING_PROFILES_ACTIVE=prod` is the backend's only runtime environment variable; the frontend's API base URL is supplied as a **build-time** argument (`VITE_API_BASE_URL`), not a runtime one, since the Vite output is static.

### Image versioning & traceability (US076)

Both services are tagged `${GIT_COMMIT:-latest}` in `docker-compose.yml`, and the `push-to-registry` CI job tags published images with both `:latest` and `:${{ github.sha }}`. Combined with the `1.0-<short-sha>` JAR versioning in `publish-artifact`, this gives three coordinated traceability points back to the source commit: the JAR, the local Compose image tag, and the registry image tag.

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
| `frontend` | Feature branch for the React Web UI work; added in Sprint 7 as a CI trigger target. |

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
fix(ci): add actions:write permissions and fork guards to workflow
fix(ci): remove deprecated p/spring ruleset from Semgrep config
```

> All commits must reference at least one issue number. Multiple issues can be referenced by chaining them: `#644 #645 #646`.

### Pull Request Process

Every PR must follow the project PR template, which includes:

- **Description** — what the PR does and why
- **Related Issues** — linked with `Closes #<number>` to auto-close issues on merge
- **Changes Made** — bullet list of the main changes
- **Checklist** — code guidelines, self-review, tests, documentation
- **Security Review Checklist** — a structured set of items reviewers must confirm before approving (US069):
  - **Input Validation** — user input is validated appropriately; invalid input is handled safely
  - **Authentication** — marked not applicable, or confirmation that authentication-related changes were reviewed
  - **Secrets** — confirmation that no credentials, passwords, or API keys were committed
  - **Logging** — logs do not contain sensitive information; error messages do not expose implementation details
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

**CI must pass** before any PR can be merged. The `pr-validation` job acts as the final gate — it only succeeds when all six upstream jobs (`build-and-test`, `static-analysis-and-security`, `secret-detection`, `dependency-scan`, `container-image-scan`, `push-to-registry`) have passed.

---

## Developer Workflow

The expected workflow for each team member is as follows:

1. **Pick up an issue** from the sprint backlog. All commits must reference at least one issue number.
2. **Work on your feature branch** (`teamA_1`, `teamA_2`, or `frontend`).
3. **Before committing**, run `./lint` locally to catch style violations early.
4. **Before committing**, run `gitleaks detect --source . --no-git --verbose` to catch any accidentally committed secrets.
5. **Before opening a PR**, run `./run` locally to verify the build and all coverage thresholds pass.
6. **If you changed a Dockerfile or `docker-compose.yml`**, build and run the affected image locally (`docker build .` / `docker compose up`) and check it starts healthy before pushing.
7. **Open a Pull Request** targeting the appropriate base branch, following the PR template, including the Security Review Checklist, and the title convention.
8. **CI must pass** before the PR can be merged. A failing pipeline (build error, test failure, coverage below threshold, secrets detected, Semgrep findings, or HIGH/CRITICAL CVEs) blocks integration; CRITICAL/HIGH container findings are currently reported but do not block (see the `container-image-scan` note above).
9. **Review artifacts** — JaCoCo, PITest, Checkstyle, PMD, OWASP, dependency-tree, and Trivy reports are all available from the Actions run page.
10. **Check PR comments** — Semgrep, OWASP, and Trivy results are automatically posted as PR comments with severity breakdowns. A consolidated quality summary is posted once all gates pass.

---

## Known Limitations & Accepted Risks (US078)

This section documents security-relevant scope decisions for the SWitCH Dev 2025/2026 delivery, so gaps are visible and traceable rather than silently absent from the documentation.

### No application-level authentication, authorization, or ACL

**Status:** Accepted risk / explicit scope decision — not an oversight.

**Description:** This delivery does not implement application-level login, authentication, authorization, or access-control-list (ACL) mechanisms. Endpoints that conceptually require a "current user" (e.g. listing a user's own transactions) currently identify the user via a `@RequestParam userId` rather than a verified session or token; this is intentionally marked with `TODO` comments in the codebase pending future JWT integration, not left undocumented.

**Decision source:** Per faculty guidance for the Sprint 7 deliverable, application-level auth/ACL was confirmed out of scope for this iteration. The affected Sprint 7 user stories were adapted accordingly rather than marked as failed or skipped:

| US | Adapted handling |
|---|---|
| US069 | The "Authentication" item in the PR Security Review Checklist is marked **Not applicable** on every PR — there is no authentication logic to review. |
| US075 | Docker Compose port/volume/env-var exposure is scoped strictly to what the app needs today; no additional auth-related ports or services were added by design. |
| US077 | "Broken Access Control" (OWASP A01) is marked **Not applicable** in the security testing scope, since there is no access-control layer to test. Coverage instead focuses on OWASP risks that *are* applicable without one: Injection (A03), Security Misconfiguration (A05), and Vulnerable/Outdated Components (A06) — see [Sprint 7 DevSecOps Traceability](#sprint-7-devsecops-traceability) below. |
| US080 | The DEI cloud deployment should document this gap explicitly and apply a compensating control at the **infrastructure** level — e.g. restricting access to the deployed services via a reverse proxy with HTTP Basic Authentication — rather than relying on the application to gate access itself. *(Not yet implemented in `docker-compose.yml` as of this writing.)* |

**Residual risk:** Without application-level authorization, any client that can reach the deployed services can call any endpoint — including ones that would normally return another user's data — as long as it supplies a plausible `userId`. For this academic delivery, the accepted mitigation is restricting *network-level* access (US080), not adding application-level access control.

**Re-evaluation trigger:** Revisit this decision once JWT-based authentication is implemented — the codebase's existing `@RequestParam userId` `TODO` markers already anticipate that change.

---

## DevSecOps Retrospective (US079)

A closing retrospective on the Sprint 7 DevSecOps work: what was implemented, what was explicitly skipped (and why), and what could be improved in a future iteration.

### Security controls implemented

| Control | Story | Notes |
|---|---|---|
| Test coverage gates (JaCoCo ≥85%, PITest ≥95%) | US033, US054 | Hard gate in `build-and-test` |
| Static analysis (Checkstyle, PMD, Semgrep) | US048 | Semgrep is a hard gate; Checkstyle/PMD are report-only |
| Secret scanning across full git history | US049 | Hard gate via Gitleaks |
| Dependency CVE scanning | US053, US071 (partial) | Hard gate via OWASP Dependency-Check |
| Severity-tagged findings posted to PRs | US056 | Semgrep, OWASP, and Trivy all post PR comments |
| Security code review checklist in PR template | US069 | Input Validation, Secrets, Logging items; Authentication marked N/A |
| Hardened Dockerfiles (pinned images, multi-stage builds, non-root users, `.dockerignore`) | US072 | Both backend and frontend images |
| Hardened container runtime (`cap_drop: ALL`, `no-new-privileges`, resource limits, healthchecks) | US074, US075 | `docker-compose.yml` |
| Image versioning & traceability (`:latest` + `:sha` tags, JAR SHA versioning) | US076 | |
| Container image vulnerability scanning | US073 | Implemented, but as a **soft gate** — see below |
| Registry publishing restricted to `main` (`push-to-registry`) | US073, US076 | Prevents unmerged PRs from overwriting `:latest` on Docker Hub; `pr-validation`'s gate condition was adjusted to tolerate the resulting skip on other branches |

### Security controls skipped — and why

| Control | Story affected | Reason |
|---|---|---|
| Application-level authentication, authorization, ACL | US069, US075, US077, US080 | Confirmed out of scope for this delivery per faculty guidance — see [Known Limitations & Accepted Risks](#known-limitations--accepted-risks-us078) |
| License-risk dependency scanning | US071 | Not implemented — OWASP Dependency-Check covers CVEs only; no license-compliance tool was added |
| Backend dependency lock file | US070 | Maven has no native lockfile equivalent to `package-lock.json`; only the frontend's dependencies are pinned this way |
| Reverse proxy / HTTP Basic Auth compensating control | US080 | Not yet implemented in `docker-compose.yml`; documented as a follow-up |

### What could be improved next iteration

- The container scan (`container-image-scan`) currently runs with `exit-code: '0'` — CRITICAL/HIGH findings are reported but never block the build, unlike every other security gate in the pipeline. Tightening this would better match US073's "scanned before acceptance" intent.
- `publish-artifact` still has no event-type or branch guard, so it runs on every successful pipeline run (not only on merges to `main`), unlike `push-to-registry`, which was restricted to `main` to stop unmerged PRs from overwriting `:latest` on Docker Hub.
- The Semgrep ruleset used to gate the build (`p/java`, `p/owasp-top-ten`) differs from the one used to generate the PR comment (which also includes `p/spring`), so posted findings counts can include results that never actually affected the gate.
- `aquasecurity/trivy-action` is pinned to `@master` rather than a release tag — the one unpinned third-party action in an otherwise version-pinned pipeline.
- `container-image-scan`'s permissions block grants `pull-requests: write` but not `issues: write`, unlike the other PR-commenting jobs; worth confirming its PR comment step actually succeeds.

### Action items for next iteration

- [x] Restrict `push-to-registry` to `main` so images are only published on merge, not on every PR — done; required adjusting `pr-validation`'s gate condition to tolerate the resulting skip
- [ ] Decide whether the Trivy container scan should become a hard gate before final delivery
- [ ] Add an HTTP Basic Auth (or equivalent) reverse-proxy layer for the DEI cloud deployment (US080)
- [ ] Investigate a license-compliance scanning tool for US071
- [ ] Align the Semgrep rulesets used by the gate and the PR-comment step
- [ ] Add an event-type/branch guard to `publish-artifact` if artifact publishing should also be restricted to `main`

---

## Sprint 7 DevSecOps Traceability

The non-functional requirements defined by US046–US057 remain in force in Sprint 7 alongside the new US068–US080. This table maps each Sprint 7 DevSecOps user story to where it is currently addressed.

> US069, US075, US077, US078, US079, and US080 were adapted per faculty guidance, since this delivery does not implement application-level login, authentication, authorization, or ACL. See [Known Limitations & Accepted Risks](#known-limitations--accepted-risks-us078) for the full rationale.

| US | Summary | Where it's addressed |
|---|---|---|
| US068 | Evidence of build, test, SAST, dependency scan, container scan | [Artifacts Produced](#artifacts-produced) table + PR comments from each job |
| US069 | Security code review checklist in PRs | [Pull Request Process](#pull-request-process) — Security Review Checklist (Input Validation, Secrets, Logging). **Authentication item marked N/A** per scope decision in [Known Limitations](#known-limitations--accepted-risks-us078) — no app-level auth exists to review |
| US070 | Dependency lock files kept and checked in | Frontend: `package-lock.json`, installed via `npm ci` in `frontend/Dockerfile`. Backend: dependency versions are managed in `pom.xml` (Maven has no native lockfile mechanism) |
| US071 | CVE and license-risk dependency scanning | CVEs: OWASP Dependency-Check in `dependency-scan`. **License-risk scanning is not currently implemented** — flagged as a gap |
| US072 | Secure Dockerfile practices (pinned images, multi-stage, `.dockerignore`, no secrets) | [Docker Images](#docker-images-us072) |
| US073 | Automated image scanning before registry upload | [`container-image-scan`](#job-container-image-scan--container-vulnerability-scan-us073) and [`push-to-registry`](#job-push-to-registry--publish-images-to-docker-hub-us073) jobs |
| US074 | Hardened container runtime (non-root, no privileged mode, limited capabilities, resource limits) | [Container Runtime Hardening](#container-runtime-hardening--compose-configuration-us074-us075-us076) — Hardened runtime settings |
| US075 | Least-privilege Compose config with health checks | [Container Runtime Hardening](#container-runtime-hardening--compose-configuration-us074-us075-us076) — Least-privilege Compose configuration. **Aligned with US080**: ports/volumes/env vars are scoped to exactly what's needed, with no auth-related exposure added |
| US076 | Versioned, traceable final images | [Container Runtime Hardening](#container-runtime-hardening--compose-configuration-us074-us075-us076) — Image versioning & traceability |
| US077 | OWASP-risk security tests applicable to the project | Covered partially via Semgrep's `p/owasp-top-ten` ruleset (Injection, Security Misconfiguration, Vulnerable Components). **Broken Access Control marked N/A** — see [Known Limitations](#known-limitations--accepted-risks-us078) |
| US078 | Unresolved findings documented with mitigation decisions | [Known Limitations & Accepted Risks](#known-limitations--accepted-risks-us078) — documents the no-auth/no-ACL scope decision |
| US079 | Final DevSecOps retrospective | [DevSecOps Retrospective](#devsecops-retrospective-us079) — implemented/skipped/improvement breakdown, including the auth/ACL scope decision |
| US080 | Secure deployment to DEI cloud via Docker Compose | `docker-compose.yml` (ports, health checks, env vars documented above). **Compensating control still to be implemented**: a reverse proxy with HTTP Basic Authentication in front of the deployed services, to avoid the app being freely accessible given the lack of app-level auth — see [Known Limitations](#known-limitations--accepted-risks-us078) |

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
| Semgrep | 1.164.0 (installed via pip in CI) |
| Gitleaks | gitleaks-action@v1 |
| OWASP Dependency-Check | 12.1.1 |
| Trivy | aquasecurity/trivy-action@master *(unpinned — see note above)* |
| Maven (backend image build stage) | 3.9.6 (Eclipse Temurin 17, Alpine) |
| Eclipse Temurin JRE (backend runtime) | 17.0.14_7 (Alpine) |
| Node.js (frontend image build stage) | 20.14.0 (Alpine) |
| nginx-unprivileged (frontend runtime) | 1.27 (Alpine) |
| JUnit Jupiter | 5.10.2 |
| Mockito | 5.10.0 |
| GitHub Actions runner | ubuntu-latest |

---