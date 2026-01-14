# ShardingSphere 5.4.x 迁移指南

## 概述

从 ShardingSphere 5.3.0 版本开始，官方移除了 Spring Boot Starter 支持，改用原生 JDBC Driver 方式集成。本指南帮助你从旧版本迁移到 5.4.x。

## 主要变更

### 1. 依赖变更

**旧版本 (5.2.x 及更早)**:
```xml
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>shardingsphere-jdbc-core-spring-boot-starter</artifactId>
    <version>5.2.1</version>
</dependency>
```

**新版本 (5.4.x)**:
```xml
<dependency>
    <groupId>io.github.shiyindaxiaojie</groupId>
    <artifactId>eden-shardingsphere-spring-boot-starter</artifactId>
</dependency>
```

### 2. 配置方式变更

**旧版本** - 在 `application.yml` 中直接配置:
```yaml
spring:
  shardingsphere:
    datasource:
      names: ds0,ds1
      ds0:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://localhost:3306/demo_ds_0
        username: root
        password: root
      ds1:
        # ...
    rules:
      sharding:
        tables:
          t_order:
            # ...
```

**新版本** - 使用独立的 YAML 配置文件:

1. 在 `application.yml` 中启用并指定配置文件:
```yaml
spring:
  shardingsphere:
    enabled: true
    config-file: classpath:shardingsphere.yaml
```

2. 创建 `shardingsphere.yaml` 配置文件:
```yaml
mode:
  type: Standalone
  repository:
    type: JDBC

dataSources:
  ds_0:
    dataSourceClassName: com.zaxxer.hikari.HikariDataSource
    driverClassName: com.mysql.cj.jdbc.Driver
    jdbcUrl: jdbc:mysql://localhost:3306/demo_ds_0
    username: root
    password: root
  ds_1:
    # ...

rules:
  - !SHARDING
    tables:
      t_order:
        # ...
```

## 迁移步骤

1. **更新依赖**: 将 `shardingsphere-jdbc-core-spring-boot-starter` 替换为 `eden-shardingsphere-spring-boot-starter`

2. **创建配置文件**: 将原来 `application.yml` 中的 ShardingSphere 配置迁移到独立的 `shardingsphere.yaml` 文件

3. **更新 application.yml**: 添加启用配置和配置文件路径

4. **测试验证**: 启动应用验证分片功能正常工作

## 配置文件路径

支持两种路径格式:
- `classpath:shardingsphere.yaml` - 类路径下的配置文件
- `file:/path/to/shardingsphere.yaml` - 绝对路径的配置文件

## 示例配置

参考 `META-INF/internal/shardingsphere-example.yaml` 获取完整的配置示例。

## 常见问题

### Q: 为什么移除了 Spring Boot Starter 支持？

A: ShardingSphere 官方从 5.3.0 版本开始移除了 Spring Boot Starter，主要原因是:
- 减少与 Spring 版本的耦合
- 统一 ShardingSphere-JDBC 和 ShardingSphere-Proxy 的配置方式
- 简化维护工作

### Q: 如何在 Spring Boot 3.x 中使用？

A: 需要额外添加 JAXB 依赖，参考 ShardingSphere 官方文档。

## 参考链接

- [ShardingSphere 5.4.1 官方文档](https://shardingsphere.apache.org/document/5.4.1/en/overview/)
- [YAML 配置参考](https://shardingsphere.apache.org/document/5.4.1/en/user-manual/shardingsphere-jdbc/yaml-config/)
- [Spring Boot 集成指南](https://shardingsphere.apache.org/document/5.4.1/en/user-manual/shardingsphere-jdbc/yaml-config/jdbc-driver/spring-boot/)
