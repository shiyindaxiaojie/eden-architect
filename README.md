<img src="https://cdn.jsdelivr.net/gh/shiyindaxiaojie/cdn/readme/icon.png" align="right" />

# Eden* Architect

[![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/cdn/readme/language-java-blue.svg)](https://github.com/shiyindaxiaojie/eden-architect)
[![Build Status](https://github.com/shiyindaxiaojie/eden-architect/workflows/build/badge.svg)](https://github.com/shiyindaxiaojie/eden-architect/actions)
[![License](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/cdn/readme/license-apache2.0-red.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![SonarCloud](https://sonarcloud.io/api/project_badges/measure?project=shiyindaxiaojie_eden-architect&metric=alert_status)](https://sonarcloud.io/dashboard?id=shiyindaxiaojie_eden-architect)

<p>
  <strong>A One-Stop Solution for Enterprise Distributed Applications</strong>
</p>

[简体中文](./README-zh-CN.md) | English

---

## 📖 Introduction

**Eden* Architect** is dedicated to providing a comprehensive solution for enterprise-level development. It encapsulates the essential components for building distributed application services. By simply adding a few annotations and minimal configuration, you can integrate Spring Boot applications into a microservices ecosystem and rapidly build distributed systems using our robust middleware capabilities.

## ✨ Key Features

- **📦 Unified Dependency Management**: Centralized management of dependency versions to resolve conflicts and encapsulated common plugins to save build time.
- **🛠️ Component Integration**: Extensions based on official Spring, integrating components like `XxlJob`, `CAT`, `Netty`, `Arthas`, and more.
- **🔌 Flexible Extension Points**: High-level abstractions for mainstream technologies, allowing dynamic adaptation of `Message Queues`, `Caching`, `SMS`, `Email`, `Excel`, etc.
- **💡 Enterprise Solutions**: Out-of-the-box solutions for `Multi-level Caching`, `Distributed Locks`, `Unique IDs`, `Idempotency`, `Auditing`, `Eventual Consistency`, and `Full-link Tracing`.

## 🏗️ Architecture

<div align="center">
  <img src="https://cdn.jsdelivr.net/gh/shiyindaxiaojie/cdn/eden-architect/component.png" alt="Architecture Diagram" width="100%" />
</div>

### Component Overview

| Component | Description |
|-----------|-------------|
| **eden-dependencies** | Manages global dependency versions. |
| **eden-parent** | Build management, encapsulates common plugins, provides out-of-the-box configuration. |
| **eden-commons** | Basic utility component, extending `Apache Commons` and `Google Guava`. |
| **eden-extensions** | Lightweight extension point component, inspired by `Dubbo` SPI. |
| **eden-cola** | Optimized `COLA` component with improved DDD models, state machines, and business extensions. |
| **eden-solutions** | Solution toolkit for `Caching`, `Locks`, `Deduplication`, `Auditing`, etc. |
| **eden-spring-framework** | Base framework supporting custom error codes and exception resolvers. |
| **eden-spring-data** | Data storage extensions for `Mybatis`, `Redis`, `Flyway`, `Liquibase`. |
| **eden-spring-security** | Auth extensions for `OAuth2`, `Jwt`, `Shiro`. |
| **eden-spring-integration** | Integration with `RocketMQ`, `Kafka`, `Netty`, `XxlJob`. |
| **eden-spring-cloud** | Cloud extensions for `Nacos`, `Sentinel`, `Zookeeper`. |

## 🚀 Getting Started

### Prerequisites

Since `Spring Boot 2.4.x` and `3.0.x` vary significantly, we maintain matching branches:

- **Branch 2.4.x**: For `Spring Boot 2.4.x` (Min JDK 1.8)
- **Branch 2.7.x**: For `Spring Boot 2.7.x` (Min JDK 11)
- **Branch 3.0.x**: For `Spring Boot 3.0.x` (Min JDK 17)

### Installation

Clone the repository and install it to your local Maven repository:

```bash
git clone https://github.com/shiyindaxiaojie/eden-architect.git
cd eden-architect
./mvnw install -T 4C
```

### Usage

1. **Add Parent POM**:
   Reference `eden-parent` in your project's `pom.xml`.

   ```xml
   <parent>
       <groupId>io.github.shiyindaxiaojie</groupId>
       <artifactId>eden-parent</artifactId>
       <version>0.0.1-SNAPSHOT</version>
       <relativePath/>
   </parent>
   ```

2. **Add Dependencies**:
   Select the starters you need (e.g., for CAT integration).

   ```xml
   <dependencies>
       <dependency>
           <groupId>io.github.shiyindaxiaojie</groupId>
           <artifactId>eden-cat-spring-boot-starter</artifactId>
       </dependency>
   </dependencies>
   ```
   *> Note: Version numbers are managed by `eden-parent`.*

3. **Configuration**:
   Enable features in `application.yml`.

   ```yaml
   cat:
     enabled: true # Enable CAT
     trace-mode: true
     servers: localhost
   ```

4. **Run**:
   Start your application. Tracing and other features will be active automatically.

   <img src="https://cdn.jsdelivr.net/gh/shiyindaxiaojie/cdn/cat/tracing.png" alt="Tracing Demo" />

## 🧩 Demos

We provide sample projects demonstrating different architectural styles:

- **[eden-demo-cola](https://github.com/shiyindaxiaojie/eden-demo-cola)**: Domain-Driven Design (COLA) architecture.
- **[eden-demo-layer](https://github.com/shiyindaxiaojie/eden-demo-layer)**: Traditional Layered architecture.
- **[eden-demo-mvc](https://github.com/shiyindaxiaojie/eden-demo-mvc)**: Simple MVC architecture for single-node apps.

## 📅 Versioning

We follow Semantic Versioning `x.y.z`:
- **x**: Major version (0 for incubation).
- **y**: Minor version.
- **z**: Patch version.

## 📝 Changelog

Please see [CHANGELOG.md](https://github.com/shiyindaxiaojie/eden-architect/blob/main/CHANGELOG.md) for details.

## 📄 License

This project is licensed under the [Apache-2.0 License](https://www.apache.org/licenses/LICENSE-2.0.html).
