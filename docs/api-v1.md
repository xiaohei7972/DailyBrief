# REST API v1

Base path: `/api/v1`

This document records the first public API surface. M0 does not implement the business endpoints yet.

| Method | Path | Purpose |
| --- | --- | --- |
| GET | `/briefings/today` | Get today's briefing |
| GET | `/briefings/{date}` | Get briefing by date |
| GET | `/briefings` | List briefing history |
| GET | `/articles/{id}` | Get article details |
| GET | `/preferences` | Get preferences |
| PUT | `/preferences` | Update preferences |
| GET | `/favorites` | List favorites |
| POST | `/favorites/{id}` | Add favorite |
| DELETE | `/favorites/{id}` | Remove favorite |

Operational endpoint:

```http
GET /actuator/health
```

## API Rules

- JSON is the default payload format.
- Public API changes must remain versioned.
- AI provider details must not leak into client-facing domain contracts unless they are explicitly diagnostic metadata.
- Server errors will later use one consistent problem/error format.
