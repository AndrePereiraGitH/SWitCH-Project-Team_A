# MITE LOVERS - TEAM A


# DevSecOps Process Documentation

**Project:** MiteLovers — SWitCH Dev 2025/2026

---

## Overview

This document describes the DevSecOps workflow adopted by Team A. It covers the local development scripts, static analysis tools, test coverage enforcement, and the automated CI pipeline. The goal is to ensure every change is built, linted, tested, and validated before being integrated into shared branches.

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

| Rule | Description                                                                  |
|---|------------------------------------------------------------------------------|
| `EmptyBlock` | No empty code blocks (e.g. empty catch blocks)                               |
| `MethodLength` | Methods must not exceed 40 lines                                             |
| `CyclomaticComplexity` | Method complexity must not exceed 15 branches/paths                          |
| `InterfaceIsType` | Interfaces must define types, not just constants                             |
| `MethodName` | Method names must follow lowerCamelCase (`^[a-z][a-zA-Z0-9]*$`)              |
| `JavadocType` | Public classes must have a JavaDoc comment                                   |
| `StringLiteralEquality` | Strings must be compared with `.equals()`, not `==`                          |
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

The pipeline runs automatically on every **Pull Request** that targets any of the following branches:

- `main`
- `teamA_1`
- `teamA_2`

It is triggered when a PR is **opened** or **updated** (new commits pushed).

### Pipeline Steps

```
Code checkout
     ↓
Install Java 17 (Temurin, with Maven cache)
     ↓
mvn clean verify
  ├── Compile
  ├── Run tests (Surefire)
  ├── JaCoCo: measure line coverage → fail if < 85%
  └── PITest: measure mutation coverage → fail if < 95%
     ↓
Upload JaCoCo report (artifact)
     ↓
Upload PITest report (artifact)
```

### Artifacts

After each pipeline run (even if the build fails), two reports are uploaded and accessible from the GitHub Actions run page:

| Artifact | Path | Description |
|---|---|---|
| `jacoco-report` | `target/site/jacoco/` | HTML line coverage report |
| `pitest-report` | `target/pit-reports/` | HTML + XML mutation coverage report |

---

## Developer Workflow

The expected workflow for each team member is as follows:

1. **Pick up an issue** from the sprint backlog. All commits must reference at least one issue number.
2. **Work on your feature branch**
3. **Before committing**, run `./lint` locally to catch style violations early.
4. **Before opening a PR**, run `./run` locally to verify the build and all coverage thresholds pass.
5. **Open a Pull Request** targeting the appropriate base branch. This triggers the CI pipeline automatically.
6. **CI must pass** before the PR can be merged. A failing pipeline (build error, test failure, or coverage below threshold) blocks integration.
7. **Review** the JaCoCo and PITest reports attached to the CI run if the build fails.

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
| JUnit Jupiter | 5.10.2 |
| Mockito | 5.10.0 |
| GitHub Actions runner | ubuntu-latest |

---