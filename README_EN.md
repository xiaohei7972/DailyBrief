# DailyBrief

[中文](README.md)

DailyBrief is an open-source, AI-powered, customizable personal daily briefing system. It automatically collects information, deduplicates and filters it, uses AI to organize it, produces structured daily briefings, and delivers them through an Android app with history, favorites, preferences, offline support, and notifications.

> Current stage: **M0 Repository Foundation**
>
> Next stage: **M1 End-to-End — return a briefing from the server and render it in Android**

## Core Goals

- Collect information automatically every day
- Normalize, deduplicate, filter, and classify content
- Use AI for summarization, keyword extraction, and importance ranking
- Generate and persist structured daily briefings
- Deliver and display briefings in an Android app
- Support history, favorites, preferences, offline access, and notifications
- Support multiple AI providers, with Gemini as the default

## Design Principles

1. **AI is not a source of facts.** Facts come from RSS, Atom, GitHub, official APIs, and other explicit sources.
2. **Providers are decoupled from business logic.** Business code depends on a common AI abstraction, not Gemini directly.
3. **Models are configurable.** Provider, model name, timeouts, and related options are configuration.
4. **Secrets stay on the server.** API keys belong in server environment variables or GitHub Secrets. The Android app must not hold AI provider keys.
5. **Prompts are versioned.** Prompts live under `server/src/main/resources/prompts/` and are committed to source control.
6. **Structured output first.** Prefer JSON outputs over parsing free-form AI text.
7. **Start as a modular monolith.** Microservices are intentionally out of scope for the first stage.

## Pipeline

```text
Sources
  ↓
Fetch
  ↓
Normalize
  ↓
Deduplicate
  ↓
Filter
  ↓
AI Classification
  ↓
AI Summarization
  ↓
Importance
  ↓
Briefing Composer
  ↓
MySQL
  ↓
REST API
  ↓
Android App
```

## Technology Stack

### Server

- Java 21
- Spring Boot
- REST API
- MySQL
- Flyway
- Spring Data JPA
- JUnit
- Mockito
- Testcontainers
- Docker

### Android

- Kotlin
- Jetpack Compose
- Material 3
- MVVM
- Retrofit + OkHttp
- Room
- DataStore
- Hilt
- WorkManager

### AI

Default provider:

- Gemini

Planned alternatives:

- OpenRouter
- Ollama

AI responsibilities include:

- Summarization
- Classification
- Keyword extraction
- Importance scoring
- Final briefing composition
- Translation when needed

## Repository Layout

```text
DailyBrief/
├── app/                    # Android App
├── server/                 # Spring Boot Server
├── docs/                   # Architecture, development, and API docs
├── deploy/                 # Docker / deployment files
├── .github/
│   └── workflows/          # CI / Security
├── README.md               # 中文
├── README_EN.md            # English
├── LICENSE
├── CONTRIBUTING.md
└── .gitignore
```

The server is organized by business domain:

```text
article/
source/
briefing/
ai/
preference/
favorite/
scheduler/
notification/
common/
```

Target AI provider structure:

```text
AiProvider
├── GeminiAiProvider
├── OpenRouterAiProvider
└── OllamaAiProvider
```

## Initial Data Model

Planned core tables:

- `source`
- `article`
- `briefing`
- `briefing_item`
- `preference`
- `favorite`
- `ai_run`
- `job_run`

The `ai_run` table will record provider, model, operation, prompt_version, token usage, latency, status, error_code, and created_at for cost, quality, and reliability analysis.

## API v1

Initial API plan:

```http
GET    /api/v1/briefings/today
GET    /api/v1/briefings/{date}
GET    /api/v1/briefings
GET    /api/v1/articles/{id}
GET    /api/v1/preferences
PUT    /api/v1/preferences
GET    /api/v1/favorites
POST   /api/v1/favorites/{id}
DELETE /api/v1/favorites/{id}
GET    /actuator/health
```

Business endpoints will be implemented milestone by milestone. M0 only establishes the project skeleton and health-check foundation.

## Android MVP

Initial screens:

- Today
- History
- Favorites
- Settings

The first notification implementation will use:

```text
WorkManager polls the server
→ detect a new briefing
→ store it in Room
→ show a local Android notification
```

FCM can be added later.

## Local Development

### Server

Requirements:

- JDK 21
- Maven 3.9+

```bash
cd server
mvn verify
mvn spring-boot:run
```

Health check:

```text
GET http://localhost:8080/actuator/health
```

Database auto-configuration is intentionally disabled in M0. MySQL, Flyway, and JPA will be connected in M2 Database.

### Android

Requirements:

- JDK 21
- Android SDK
- Gradle 8.9

```bash
cd app
gradle lintDebug testDebugUnitTest assembleDebug
```

## Configuration and Secrets

Never commit real API keys.

Planned server environment variables include:

```text
DAILYBRIEF_AI_PROVIDER=gemini
GEMINI_API_KEY=...
GEMINI_MODEL=...
OPENROUTER_API_KEY=...
OLLAMA_BASE_URL=http://localhost:11434
```

The Android app must never directly read or persist server-side Gemini, OpenRouter, or other provider secrets.

## Prompt Versions

```text
server/src/main/resources/prompts/
└── v1/
    ├── classify.txt
    ├── summarize.txt
    ├── rank.txt
    └── briefing.txt
```

When prompt behavior changes materially, preserve version boundaries so production output semantics remain traceable.

## CI and Security

M0 configures:

- Server CI: Java 21, Maven verify, JAR artifact
- Android CI: lint, unit tests, assembleDebug, APK artifact
- CodeQL: Java / Kotlin static analysis
- Dependabot: Maven, Gradle, and GitHub Actions dependency updates

## Roadmap

| Milestone | Scope |
| --- | --- |
| M0 Foundation | Repository layout, bilingual README, Server/App skeletons, CI, CodeQL, docs |
| M1 End-to-End | Hard-coded briefing → REST → Retrofit → ViewModel → Compose |
| M2 Database | MySQL + Flyway + Briefing persistence |
| M3 Sources | RSS / Atom / GitHub → Article |
| M4 Gemini | Gemini Provider + JSON Schema + Prompt Version |
| M5 Pipeline | Source → Filter → Gemini → automated Briefing |
| M6 Android MVP | Today / History / Favorites / Settings |
| M7 Offline | Room + WorkManager |
| M8 Notification | Local notifications for new briefings |
| M9 Reliability | Retry / Timeout / Logging / Metrics / ai_run / job_run |
| M10 Release | v0.1.0 |

Version targets:

```text
v0.0.x       project skeleton
v0.1.0-alpha generate and view briefings
v0.2.0-alpha multiple sources, favorites, settings
v0.3.0-beta  notifications, offline support, reliability
v0.5.0-beta  multiple AI providers
v1.0.0       Stable
```

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).

## License

MIT License. See [LICENSE](LICENSE).
