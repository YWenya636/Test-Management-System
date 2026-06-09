# 测试用例管理平台

一个轻量级测试用例管理平台，提供用例增删改查、执行记录追踪、用例状态管理功能。

## 技术栈
- Spring Boot 3.2.5
- MyBatis
- H2/MySQL
- JUnit 5
- JaCoCo

## 运行项目
```bash
mvn spring-boot:run
- 测试 CI

## GitHub Actions 与 Jenkins 对比

| 对比维度 | GitHub Actions | Jenkins |
|---------|---------------|---------|
| 部署方式 | 云端托管,由 GitHub 提供运行环境,无需自建服务器,仓库加配置即可用 | 自托管,需自己准备服务器并安装、部署、维护;采用主节点 + Agent 从节点架构 |
| 配置文件 | YAML 文件,放在 `.github/workflows/` 目录下,声明式语法,随代码一起版本管理 | 早期靠 Web 界面配置,现主流用 `Jenkinsfile`(Groovy 语法)实现 Pipeline as Code |
| 学习曲线 | 平缓,YAML 语法简单、文档友好,与 GitHub 原生集成,容易上手 | 较陡,需掌握节点管理、Pipeline、Groovy 脚本及大量插件配置 |
| 典型用户 | 代码托管在 GitHub 的个人开发者、开源项目、中小团队,追求快速落地 | 大型企业、需要复杂流水线、私有化部署或强管控的团队 |
| 成本 | 公开仓库免费;私有仓库有免费额度,超出按运行分钟数计费,无运维成本 | 软件开源免费,但服务器、运维人力是隐性成本,长期投入较高 |

