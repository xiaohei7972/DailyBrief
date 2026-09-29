# Architecture

## 1. System Boundary

DailyBrief is a modular monolith consisting of a Spring Boot server and an Android client.

The server owns source ingestion, normalization, deduplication, filtering, AI orchestration, briefing composition, persistence, and public REST APIs.

The Android client consumes the REST API and owns presentation, local cache, user-facing settings, background polling, and local notifications.

## 2. Pipeline

```text
Source -> Fetch -> Normalize -> Deduplicate -> Filter
       -> AI Classify -> AI Summarize -> Rank
       -> Briefing Composer -> MySQL -> REST -> Android
```

AI enriches source material but is never treated as the source of factual claims.

## 3. Server Domains

```text
article/       normalized content and article lifecycle
source/        RSS / Atom / GitHub / official API connectors
briefing/      briefing model and composition
ai/            provider abstraction, operations, structured outputs
preference/    user briefing preferences
favorite/      saved articles / briefing items
scheduler/     scheduled jobs
notification/  notification-related server concerns
common/        shared technical primitives only
```

Avoid global controller/service/entity buckets. A domain may contain its own API, application service, model, repository, and mapper when needed.

## 4. AI Boundary

The target abstraction is:

```text
AiProvider
├── GeminiAiProvider
├── OpenRouterAiProvider
└── OllamaAiProvider
```

Business services call an operation-oriented AI application layer. Provider adapters are responsible for provider SDK/HTTP details.

Provider selection and model names are configuration. Server-side secrets never cross the REST boundary to Android.

## 5. Structured AI Output

AI operations should return validated structured objects. JSON Schema or equivalent validation should be used before an AI result enters the domain pipeline.

Prompt files are versioned under:

```text
server/src/main/resources/prompts/<version>/
```

Every persisted AI run should be traceable to provider, model, operation, and prompt version.

## 6. Persistence Plan

Initial tables:

`source`, `article`, `briefing`, `briefing_item`, `preference`, `favorite`, `ai_run`, `job_run`.

Flyway owns schema evolution. JPA owns application persistence mappings.

## 7. Android Architecture

The Android app uses MVVM.

```text
Compose UI
   ↓
ViewModel
   ↓
Repository
   ├── Retrofit / OkHttp
   └── Room
```

DataStore stores lightweight preferences. WorkManager performs background polling and later local notification scheduling.

## 8. Milestone Boundary

M0 creates buildable foundations only. M1 introduces the first real end-to-end briefing endpoint and Android data flow. M2 enables database persistence.
