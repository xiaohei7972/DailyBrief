# Contributing to DailyBrief

感谢你参与 DailyBrief。

## 开发原则

- 保持 Server 与 Android 的职责边界清晰。
- AI Provider 必须通过抽象接入，不得把 Gemini 逻辑写入业务领域代码。
- AI 不作为新闻事实来源。
- 不提交 API Key、Token、密码或其他密钥。
- Prompt 修改必须保留可追踪的版本。
- 优先提交小而清晰、可测试的 PR。

## 分支建议

- `feat/<scope>`
- `fix/<scope>`
- `refactor/<scope>`
- `docs/<scope>`
- `ci/<scope>`

## Server

```bash
cd server
mvn verify
```

## Android

```bash
cd app
gradle lintDebug testDebugUnitTest assembleDebug
```

## Pull Request

PR 应说明：

1. 解决的问题
2. 主要改动
3. 测试方式
4. 是否修改 API、数据库或 Prompt
5. 后续工作

提交前请确认 CI 通过。
