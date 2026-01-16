<img src="https://cdn.jsdelivr.net/gh/shiyindaxiaojie/cdn/readme/icon.png" align="right" />

# Eden* Architect

[![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/cdn/readme/language-java-blue.svg)](https://github.com/shiyindaxiaojie/eden-architect)
[![Build Status](https://github.com/shiyindaxiaojie/eden-architect/workflows/build/badge.svg)](https://github.com/shiyindaxiaojie/eden-architect/actions)
[![License](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/cdn/readme/license-apache2.0-red.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![SonarCloud](https://sonarcloud.io/api/project_badges/measure?project=shiyindaxiaojie_eden-architect&metric=alert_status)](https://sonarcloud.io/dashboard?id=shiyindaxiaojie_eden-architect)

<p>
  <strong>🚀 A One-Stop Solution for Enterprise Distributed Applications</strong>
</p>

English | [简体中文](./README-zh-CN.md)

---

## 📖 Introduction

**Eden* Architect** is dedicated to providing a comprehensive solution for enterprise-level development. It encapsulates the essential components for building distributed application services. By simply adding a few annotations and minimal configuration, you can integrate Spring Boot applications into a microservices ecosystem and rapidly build distributed systems using our robust middleware capabilities.

## 📚 Documentation

- [English Documentation](./docs/en/README.md) - Component Integration Guides
- [中文文档](./docs/zh-CN/README.md) - 组件集成指南

## ✨ Key Features

| Feature | Description |
|---------|-------------|
| 📦 **Unified Dependency Management** | Centralized version management to resolve conflicts; encapsulated plugins to reduce build time |
| 🛠️ **Deep Component Integration** | Spring-based extensions with out-of-the-box integration for `XxlJob`, `CAT`, `Netty`, `Arthas` |
| 🔌 **Flexible Extension Points** | High-level abstractions for MQ, Cache, SMS, Email, Excel with dynamic adaptation |
| 💡 **Enterprise Solutions** | `Multi-level Cache`, `Distributed Lock`, `Unique ID`, `Idempotency`, `Audit Log`, `Eventual Consistency`, `Full-link Tracing` |

## 🏗️ Architecture Overview

```mermaid
graph TB
    subgraph Starters["🔧 Auto-Configuration Layer"]
        SBS["eden-spring-boot-starters"]
        SCS["eden-spring-cloud-starters"]
        SOL["eden-solutions"]
    end

    subgraph Spring["📦 Spring Component Layer"]
        SB["eden-spring-boot"]
        SC["eden-spring-cloud"]
        SEC["eden-spring-security"]
        SD["eden-spring-data"]
        SI["eden-spring-integration"]
    end

    subgraph Framework["🏛️ Framework Layer"]
        SF["eden-spring-framework"]
    end

    subgraph Foundation["🧱 Foundation Layer"]
        EXT["eden-extensions"]
        COM["eden-commons"]
    end

    subgraph COLA["🎯 COLA Architecture"]
        COLA_ALL["eden-cola"]
        CSM["eden-cola-statemachine"]
    end

    subgraph Build["🔨 Build Layer"]
        PAR["eden-parent"]
        DEP["eden-dependencies"]
    end

    Starters --> Spring --> Framework --> Foundation --> Build
    COLA --> Foundation
    EXT --> COM
    PAR --> DEP
    CSM -.-> COLA_ALL

    style Starters fill:#e3f2fd,stroke:#1976d2
    style Spring fill:#f3e5f5,stroke:#7b1fa2
    style Framework fill:#e8f5e9,stroke:#388e3c
    style Foundation fill:#fff8e1,stroke:#ffa000
    style COLA fill:#fce4ec,stroke:#c2185b
    style Build fill:#eceff1,stroke:#607d8b
```

### Component Overview

| Component | Description |
|-----------|-------------|
| **eden-dependencies** | Manages global dependency versions |
| **eden-parent** | Build management with common plugins and out-of-the-box configuration |
| **eden-commons** | Basic utilities extending `Apache Commons` and `Google Guava` |
| **eden-extensions** | Lightweight extension framework inspired by `Dubbo` SPI |
| **eden-cola** | Optimized `COLA` component with DDD models, state machines, and business extensions |
| **eden-solutions** | Solution toolkit for `Caching`, `Locks`, `Deduplication`, `Auditing` |
| **eden-spring-framework** | Base framework supporting custom error codes and exception resolvers |
| **eden-spring-data** | Data storage extensions for `Mybatis`, `Redis`, `Flyway`, `Liquibase` |
| **eden-spring-security** | Auth extensions for `OAuth2`, `Jwt`, `Shiro` |
| **eden-spring-integration** | Integration with `RocketMQ`, `Kafka`, `Netty`, `XxlJob` |
| **eden-spring-cloud** | Cloud extensions for `Nacos`, `Sentinel`, `Zookeeper` |

## 🚀 Getting Started

### Prerequisites

Since `Spring Boot 2.4.x` and `3.0.x` vary significantly, we maintain matching branches:

| Branch | Spring Boot | JDK |
|--------|-------------|-----|
| 2.4.x | 2.4.x | 8+ |
| 2.7.x | 2.7.x | 11+ |
| 3.0.x | 3.0.x | 17+ |

### Installation

```bash
git clone https://github.com/shiyindaxiaojie/eden-architect.git
cd eden-architect
./mvnw install -T 4C
```

### Usage

**1. Add Parent POM**

```xml
<parent>
    <groupId>io.github.shiyindaxiaojie</groupId>
    <artifactId>eden-parent</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <relativePath/>
</parent>
```

**2. Add Dependencies**

```xml
<dependency>
    <groupId>io.github.shiyindaxiaojie</groupId>
    <artifactId>eden-cat-spring-boot-starter</artifactId>
</dependency>
```

> Version numbers are managed by `eden-parent`.

**3. Configuration**

```yaml
cat:
  enabled: true
  trace-mode: true
  servers: localhost
```

**4. Run**

Start your application and make HTTP requests to see full-link tracing in CAT console.

## 🧩 Demo Projects

| Project | Architecture | Description |
|---------|--------------|-------------|
| [eden-demo-cola](https://github.com/shiyindaxiaojie/eden-demo-cola) | COLA | Domain-Driven Design for complex business |
| [eden-demo-layer](https://github.com/shiyindaxiaojie/eden-demo-layer) | Layered | Traditional data-centric architecture |
| [eden-demo-mvc](https://github.com/shiyindaxiaojie/eden-demo-mvc) | MVC | Simple monolithic applications |

## 🔧 Best Practices

### CAT Full-Link Tracing

Analyze the entire trace including `HTTP` latency, `RPC` details, `Log` business logs, `SQL` and `Cache` execution time via `TraceId`.

![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/cdn/cat/tracing.png)

### Sentinel Traffic Governance

Configure flow control rules based on business load, monitor interface QPS and rate limiting status.

![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/cdn/sentinel/sentinel-dashboard-overview-custom.png)

### Arthas Online Diagnostics

Use runtime probes for dynamic service discovery, out-of-the-box diagnostics in low-load environments.

![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/cdn/arthas/arthas-dashboard-overview.png)

## 📅 Versioning

We follow Semantic Versioning `x.y.z`:

| Version | Description |
|---------|-------------|
| x | Major version (0 for incubation) |
| y | Minor version (feature iterations) |
| z | Patch version (bug fixes) |

## 📝 Changelog

Please see [CHANGELOG.md](https://github.com/shiyindaxiaojie/eden-architect/blob/main/CHANGELOG.md) for details.

## 📄 License

This project is licensed under the [Apache-2.0 License](https://www.apache.org/licenses/LICENSE-2.0.html).