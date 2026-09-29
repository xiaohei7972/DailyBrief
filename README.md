# DailyBrief

[English](README_EN.md)

DailyBrief 是一个开源、AI 驱动、可自定义信息源的个人每日简报系统。它负责自动采集信息、去重过滤、AI 整理、生成结构化简报，并通过 Android App 提供阅读、历史、收藏、偏好和通知能力。

> 当前阶段：**M0 Repository Foundation**
>
> 下一阶段：**M1 End-to-End — Server 返回简报，Android 拉取并展示**

## 核心目标

- 每天自动采集信息
- 标准化、去重、过滤和分类
- 使用 AI 进行摘要、关键词提取和重要性判断
- 生成结构化每日简报并持久化
- Android App 接收并展示每日简报
- 支持历史、收藏、偏好、离线和通知
- 支持多个 AI Provider，默认使用 Gemini

## 设计原则

1. **AI 不作为事实来源。** 新闻事实来自 RSS、Atom、GitHub、官方 API 等信息源。
2. **Provider 与业务解耦。** 业务代码只依赖统一的 AI 抽象，不直接依赖 Gemini。
3. **模型配置化。** Provider、模型名、超时和相关参数通过配置管理。
4. **密钥只在服务端。** API Key 只能位于服务器环境变量或 GitHub Secrets，Android App 不持有 AI Key。
5. **Prompt 版本化。** Prompt 位于 `server/src/main/resources/prompts/` 并纳入版本控制。
6. **结构化输出优先。** AI 输出优先使用 JSON，避免依赖自由文本格式。
7. **先单体、后扩展。** 第一阶段使用模块化单体，不引入微服务。

## Pipeline

```text
信息源
  ↓
抓取
  ↓
标准化
  ↓
去重
  ↓
过滤
  ↓
AI 分类
  ↓
AI 摘要
  ↓
重要度
  ↓
Briefing Composer
  ↓
MySQL
  ↓
REST API
  ↓
Android App
```

## 技术栈

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

默认 Provider：

- Gemini

计划支持：

- OpenRouter
- Ollama

AI 的职责包括：

- 摘要
- 分类
- 关键词提取
- 重要性判断
- 最终简报整理
- 必要时翻译

## 仓库结构

```text
DailyBrief/
├── app/                    # Android App
├── server/                 # Spring Boot Server
├── docs/                   # 架构、开发和 API 文档
├── deploy/                 # Docker / 部署文件
├── .github/
│   └── workflows/          # CI / Security
├── README.md               # 中文
├── README_EN.md            # English
├── LICENSE
├── CONTRIBUTING.md
└── .gitignore
```

Server 按业务领域组织：

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

AI Provider 目标结构：

```text
AiProvider
├── GeminiAiProvider
├── OpenRouterAiProvider
└── OllamaAiProvider
```

## 第一阶段数据模型

计划中的核心表：

- `source`
- `article`
- `briefing`
- `briefing_item`
- `preference`
- `favorite`
- `ai_run`
- `job_run`

其中 `ai_run` 将记录 provider、model、operation、prompt_version、token 使用量、latency、status、error_code 和 created_at，用于成本、质量和可靠性分析。

## API v1

初步接口：

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

业务 API 将在后续里程碑逐步实现。M0 只保证项目骨架和健康检查基础可用。

## Android MVP

第一阶段页面：

- 今日
- 历史
- 收藏
- 设置

第一版通知采用：

```text
WorkManager 定时请求后端
→ 发现新简报
→ 写入 Room
→ Android 本地通知
```

后续再增加 FCM。

## 本地开发

### Server

要求：

- JDK 21
- Maven 3.9+

```bash
cd server
mvn verify
mvn spring-boot:run
```

健康检查：

```text
GET http://localhost:8080/actuator/health
```

M0 暂未启用数据库自动配置；MySQL、Flyway 和 JPA 会在 M2 Database 接入。

### Android

要求：

- JDK 21
- Android SDK
- Gradle 8.9

```bash
cd app
gradle lintDebug testDebugUnitTest assembleDebug
```

## 配置与密钥

禁止把真实 API Key 提交到仓库。

服务端环境变量规划示例：

```text
DAILYBRIEF_AI_PROVIDER=gemini
GEMINI_API_KEY=...
GEMINI_MODEL=...
OPENROUTER_API_KEY=...
OLLAMA_BASE_URL=http://localhost:11434
```

Android App 不允许直接读取或保存 Gemini / OpenRouter 等 Provider 的服务端密钥。

## Prompt 版本

```text
server/src/main/resources/prompts/
└── v1/
    ├── classify.txt
    ├── summarize.txt
    ├── rank.txt
    └── briefing.txt
```

修改 Prompt 时应保留版本边界，避免在不记录版本的情况下直接改变线上输出语义。

## CI 与安全

M0 配置：

- Server CI：Java 21、Maven verify、JAR Artifact
- Android CI：lint、unit test、assembleDebug、APK Artifact
- CodeQL：Java / Kotlin 静态分析
- Dependabot：Maven、Gradle、GitHub Actions 依赖更新

## 路线图

| Milestone | 内容 |
| --- | --- |
| M0 Foundation | 仓库结构、双语 README、Server/App 骨架、CI、CodeQL、docs |
| M1 End-to-End | 写死简报 → REST → Retrofit → ViewModel → Compose |
| M2 Database | MySQL + Flyway + Briefing 持久化 |
| M3 Sources | RSS / Atom / GitHub → Article |
| M4 Gemini | Gemini Provider + JSON Schema + Prompt Version |
| M5 Pipeline | Source → Filter → Gemini → Briefing 自动化 |
| M6 Android MVP | 今日 / 历史 / 收藏 / 设置 |
| M7 Offline | Room + WorkManager |
| M8 Notification | 新简报本地通知 |
| M9 Reliability | Retry / Timeout / Logging / Metrics / ai_run / job_run |
| M10 Release | v0.1.0 |

版本目标：

```text
v0.0.x       项目骨架
v0.1.0-alpha 可以生成和查看简报
v0.2.0-alpha 多信息源、收藏、设置
v0.3.0-beta  通知、离线、稳定性
v0.5.0-beta  多 AI Provider
v1.0.0       Stable
```

## 贡献

请阅读 [CONTRIBUTING.md](CONTRIBUTING.md)。

## License

MIT License。详见 [LICENSE](LICENSE)。
