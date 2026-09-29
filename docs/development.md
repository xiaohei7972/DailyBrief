# Development Guide

## Prerequisites

### Server

- JDK 21
- Maven 3.9+
- Docker for later Testcontainers / MySQL work

### Android

- JDK 21
- Android SDK
- Gradle 8.9

## Server Commands

```bash
cd server
mvn verify
mvn spring-boot:run
```

M0 intentionally excludes datasource, JPA, and Flyway auto-configuration at runtime. M2 will enable these integrations.

## Android Commands

```bash
cd app
gradle lintDebug
gradle testDebugUnitTest
gradle assembleDebug
```

## Configuration

Do not commit real secrets.

Server-side secrets are expected through environment variables or secret managers. GitHub Actions secrets are used for CI/CD secrets when required.

Android must not contain AI provider API keys.

## Pull Request Workflow

1. Branch from `main`.
2. Keep changes focused.
3. Run relevant local verification.
4. Open a PR.
5. Let required CI complete.
6. Merge into `main`.

## Package Organization

Server code is grouped by domain, not by global technical layer. Add new code inside the relevant domain package unless it is genuinely shared infrastructure.

## Prompt Changes

Prompt files are production behavior. Treat prompt edits like code changes: review them, version material behavior changes, and cover parsing/validation with tests when AI integration begins.
