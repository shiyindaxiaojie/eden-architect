# Spring Boot 2.7.18 -> 3.5.9 升级指南

## 一、概述

本文档详细记录 Eden* Architect 从 Spring Boot 2.7.18 升级到 3.5.9 所有必须修改的配置和代码。这是一次重大升级，涉及 Java 版本、命名空间迁移（javax → jakarta）、Spring Framework 6.x 等核心变更。

## 二、版本演进

Spring Boot 版本演进如下（🔴 不兼容 | 🟡 废弃 | 🔵 安全修复 | 🟢 Bug 修复）

| 版本 | 变更类型 | 变更内容 |
|------|----------|----------|
| 3.0.0 | 🔴 不兼容 | 最低要求 JDK 17 |
| 3.0.0 | 🔴 不兼容 | `javax.*` 命名空间迁移到 `jakarta.*` |
| 3.0.0 | 🔴 不兼容 | Spring Framework 升级到 6.0.x |
| 3.0.0 | 🔴 不兼容 | `spring.redis.*` 迁移到 `spring.data.redis.*` |
| 3.0.0 | 🔴 不兼容 | Hibernate 升级到 6.x，JPA 规范升级到 3.1 |
| 3.0.0 | 🔴 不兼容 | 移除 `spring-security-oauth2` 支持 |
| 3.0.0 | 🟡 废弃 | `@ConstructorBinding` 不再需要显式声明 |
| 3.1.0 | 🔴 不兼容 | Servlet 6.0 / Tomcat 10.1 |
| 3.1.0 | 🟢 修复 | 改进 GraalVM 原生镜像支持 |
| 3.2.0 | 🔴 不兼容 | 虚拟线程支持（需 JDK 21） |
| 3.2.0 | 🟡 废弃 | `RestTemplate` 推荐使用 `RestClient` |
| 3.3.0 | 🟢 修复 | CDS (Class Data Sharing) 支持 |
| 3.4.0 | 🔴 不兼容 | 结构化日志支持 |
| 3.5.0 | 🟢 修复 | 性能优化和 Bug 修复 |
| 3.5.9 | 🟢 修复 | 最新维护版本 |

## 三、升级清单

### 3.1 核心框架升级

| 项目 | 旧版本 | 新版本 | 必须 |
|------|--------|--------|------|
| JDK | 11 | 17+ | ✅ |
| Spring Boot | 2.7.18 | 3.5.9 | ✅ |
| Spring Framework | 5.3.31 | 6.2.x | ✅ |
| Spring Cloud | 2021.0.9 | 2024.0.1 | ✅ |
| Spring Cloud Alibaba | 2021.0.5.0 | 2023.0.1.0 | ✅ |
| Spring Security | 5.7.x | 6.4.x | ✅ |

### 3.2 命名空间迁移 (javax → jakarta)

| 旧包名 | 新包名 | 说明 |
|--------|--------|------|
| `javax.servlet.*` | `jakarta.servlet.*` | Servlet API |
| `javax.persistence.*` | `jakarta.persistence.*` | JPA API |
| `javax.validation.*` | `jakarta.validation.*` | Bean Validation |
| `javax.annotation.*` | `jakarta.annotation.*` | Common Annotations |
| `javax.transaction.*` | `jakarta.transaction.*` | JTA |
| `javax.mail.*` | `jakarta.mail.*` | JavaMail |
| `javax.websocket.*` | `jakarta.websocket.*` | WebSocket |

> **注意**: `javax.sql.*`、`javax.naming.*`、`javax.crypto.*` 等属于 Java SE，无需迁移。

### 3.3 数据库升级

| 依赖 | 旧版本 | 新版本 |
|------|--------|--------|
| mybatis | 3.5.16 | 3.5.19 |
| mybatis-spring | 2.1.2 | 3.0.4 |
| mybatis-spring-boot | 2.3.2 | 3.0.4 |
| mybatis-plus | 3.5.7 | 3.5.9 |
| hibernate | 5.6.15.Final | 6.6.0.Final |
| hikaricp | 4.0.3 | 5.1.0 |
| druid | 1.2.20 | 1.2.23 |
| shardingsphere | 5.4.1 | 5.5.1 |
| dynamic-datasource | 3.6.1 | 4.3.1 |
| liquibase | 4.17.2 | 4.29.2 |

### 3.4 中间件升级

| 依赖 | 旧版本 | 新版本 |
|------|--------|--------|
| nacos-client | 2.1.2 | 2.3.2 |
| sentinel | 1.8.6 | 1.8.8 |
| dubbo | 3.2.14 | 3.3.0 |
| redisson | 3.32.0 | 3.40.2 |
| rocketmq | 4.9.7 | 5.1.4 |
| rocketmq-spring-boot | 2.2.3 | 2.3.1 |
| kafka | 2.7.2 | 3.8.1 |
| spring-kafka | 2.7.9 | 3.3.1 |

### 3.5 工具库升级

| 依赖 | 旧版本 | 新版本 |
|------|--------|--------|
| lombok | 1.18.30 | 1.18.36 |
| mapstruct | 1.5.5.Final | 1.6.3 |
| guava | 32.1.2-jre | 33.3.1-jre |
| jackson | 2.14.3 | 2.18.2 |
| fastjson2 | 2.0.43 | 2.0.53 |
| easyexcel | 3.3.3 | 4.0.3 |
| netty | 4.1.100.Final | 4.1.115.Final |
| log4j2 | 2.20.0 | 2.24.2 |

### 3.6 测试框架升级

| 依赖 | 旧版本 | 新版本 |
|------|--------|--------|
| junit-jupiter | 5.9.3 | 5.11.3 |
| mockito | 4.8.1 | 5.14.2 |
| testcontainers | 1.18.3 | 1.20.4 |
| spock | 2.3-groovy-4.0 | 2.4-groovy-4.0 |

#### 3.6.1 Spring Kafka Test API 变更

Spring Kafka Test 在 3.x 版本中对嵌入式 Kafka 测试组件进行了重大重构：

| 变更项 | 旧版本 (2.x) | 新版本 (3.x) | 说明 |
|--------|-------------|-------------|------|
| EmbeddedKafkaBroker | 具体类 | 抽象类 | 不能直接实例化 |
| 实现类 | - | EmbeddedKafkaZKBroker | 使用 Zookeeper 模式 |
| 实现类 | - | EmbeddedKafkaKraftBroker | 使用 KRaft 模式（无需 Zookeeper） |
| zkPort() 方法 | 存在 | 已移除 | Kafka 3.x 支持 KRaft 模式 |

### 3.7 监控组件升级

| 依赖 | 旧版本 | 新版本 |
|------|--------|--------|
| micrometer | 1.9.17 | 1.14.2 |
| spring-boot-admin | 2.7.10 | 3.4.1 |
| zipkin | 2.23.18 | 2.27.1 |
| brave | 5.13.10 | 6.0.3 |

### 3.8 认证授权升级

| 依赖 | 旧版本 | 新版本 |
|------|--------|--------|
| spring-authorization-server | 0.2.3 | 1.4.1 |
| jjwt | 0.11.5 | 0.12.6 |

### 3.9 其他依赖升级

| 依赖 | 旧版本 | 新版本 |
|------|--------|--------|
| curator | 5.4.0 | 5.7.1 |
| zookeeper | 3.8.4 | 3.9.3 |
| grpc | 1.58.0 | 1.68.2 |
| protobuf | 4.0.0-rc-2 | 4.28.3 |
| arthas | 3.7.2 | 4.0.4 |
| xxl-job | 2.4.0 | 2.4.1 |

---

## 四、升级步骤

### 4.1 JDK 版本升级

```xml
<!-- pom.xml -->
<properties>
    <!-- ❌ 旧配置 -->
    <java.version>11</java.version>
    
    <!-- ✅ 新配置 -->
    <java.version>17</java.version>
</properties>
```

### 4.2 javax → jakarta 命名空间迁移

#### 4.2.1 Servlet API

```java
// ❌ 旧代码 - 2.7.x
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// ✅ 新代码 - 3.5.x
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
```

#### 4.2.2 JPA/Persistence API

```java
// ❌ 旧代码 - 2.7.x
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;

// ✅ 新代码 - 3.5.x
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
```

#### 4.2.3 Validation API

```java
// ❌ 旧代码 - 2.7.x
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

// ✅ 新代码 - 3.5.x
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
```

#### 4.2.4 Annotation API

```java
// ❌ 旧代码 - 2.7.x
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

// ✅ 新代码 - 3.5.x
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
```

### 4.3 配置文件变更

#### 4.3.1 Redis 配置路径

```yaml
# ❌ 旧配置 - 2.7.x (仍支持但已废弃)
spring:
  redis:
    host: localhost
    port: 6379
    password: 
    database: 0
    lettuce:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0

# ✅ 新配置 - 3.5.x
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password: 
      database: 0
      lettuce:
        pool:
          max-active: 8
          max-idle: 8
          min-idle: 0
```

#### 4.3.2 Hibernate 配置

```yaml
# ❌ 旧配置 - 2.7.x
spring:
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL5InnoDBDialect

# ✅ 新配置 - 3.5.x (Hibernate 6.x 自动检测方言)
spring:
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect
```

#### 4.3.3 Actuator 配置

```yaml
# 3.5.x 新增配置选项
management:
  observations:
    key-values:
      application: ${spring.application.name}
  tracing:
    sampling:
      probability: 1.0
```

### 4.4 Spring Security 代码变更

#### 4.4.1 SecurityFilterChain 配置

```java
// ❌ 旧代码 - 2.7.x
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .antMatchers("/api/public/**").permitAll()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            );
        return http.build();
    }
}

// ✅ 新代码 - 3.5.x
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // antMatchers 改为 requestMatchers
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            );
        return http.build();
    }
}
```

#### 4.4.2 方法级安全

```java
// ❌ 旧代码 - 2.7.x
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)

// ✅ 新代码 - 3.5.x (jsr250Enabled 默认为 false)
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
```

### 4.5 Hibernate 6.x 代码变更

#### 4.5.1 ID 生成策略

```java
// ❌ 旧代码 - Hibernate 5.x
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}

// ✅ 新代码 - Hibernate 6.x (推荐使用 SEQUENCE 或 UUID)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_sequence", allocationSize = 1)
    private Long id;
}
```

#### 4.5.2 Query 变更

```java
// ❌ 旧代码 - Hibernate 5.x
Query query = session.createQuery("from User where name = :name");
query.setParameter("name", name);
List<User> users = query.list();

// ✅ 新代码 - Hibernate 6.x
TypedQuery<User> query = session.createQuery("from User where name = :name", User.class);
query.setParameter("name", name);
List<User> users = query.getResultList();
```

### 4.6 MyBatis 代码变更

#### 4.6.1 mybatis-spring-boot-starter 3.x

```xml
<!-- ❌ 旧依赖 - 2.7.x -->
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.3.2</version>
</dependency>

<!-- ✅ 新依赖 - 3.5.x -->
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>3.0.4</version>
</dependency>
```

### 4.7 RestClient 替代 RestTemplate

```java
// ❌ 旧代码 - 2.7.x (RestTemplate)
@Service
public class UserService {
    
    private final RestTemplate restTemplate;
    
    public User getUser(Long id) {
        return restTemplate.getForObject("/api/users/{id}", User.class, id);
    }
}

// ✅ 新代码 - 3.5.x (RestClient)
@Service
public class UserService {
    
    private final RestClient restClient;
    
    public UserService(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("http://user-service").build();
    }
    
    public User getUser(Long id) {
        return restClient.get()
            .uri("/api/users/{id}", id)
            .retrieve()
            .body(User.class);
    }
}
```

### 4.8 Observability 配置

```java
// 3.5.x 新增 Micrometer Observation API
@Configuration
public class ObservabilityConfig {
    
    @Bean
    public ObservationRegistry observationRegistry() {
        return ObservationRegistry.create();
    }
}
```

### 4.9 Spring Kafka Test 嵌入式 Broker 变更

#### 4.9.1 EmbeddedKafkaBroker 实例化

```java
// ❌ 旧代码 - Spring Kafka 2.x
import org.springframework.kafka.test.EmbeddedKafkaBroker;

public class EmbeddedKafka {
    private final EmbeddedKafkaBroker kafkaBroker;
    
    public EmbeddedKafka() {
        // 直接实例化（2.x 中是具体类）
        kafkaBroker = new EmbeddedKafkaBroker(1);
        kafkaBroker.kafkaPorts(9092);
        kafkaBroker.zkPort(2181);  // 配置 Zookeeper 端口
    }
}

// ✅ 新代码 - Spring Kafka 3.x
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.EmbeddedKafkaZKBroker;

public class EmbeddedKafka {
    private final EmbeddedKafkaBroker kafkaBroker;
    
    public EmbeddedKafka() {
        // 使用具体实现类（3.x 中 EmbeddedKafkaBroker 是抽象类）
        kafkaBroker = new EmbeddedKafkaZKBroker(1, false);
        kafkaBroker.kafkaPorts(9092);
        // zkPort() 方法已移除，Zookeeper 端口由 Broker 自动管理
    }
}
```

#### 4.9.2 使用 KRaft 模式（推荐）

```java
// ✅ 新代码 - Spring Kafka 3.x (KRaft 模式，无需 Zookeeper)
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.EmbeddedKafkaKraftBroker;

public class EmbeddedKafka {
    private final EmbeddedKafkaBroker kafkaBroker;
    
    public EmbeddedKafka() {
        // 使用 KRaft 模式（Kafka 3.x 推荐方式）
        kafkaBroker = new EmbeddedKafkaKraftBroker(1);
        kafkaBroker.kafkaPorts(9092);
    }
}
```

#### 4.9.3 测试类中使用 @EmbeddedKafka

```java
// ✅ 推荐方式 - 使用注解自动配置
@SpringBootTest
@EmbeddedKafka(
    partitions = 1,
    topics = {"test-topic"},
    bootstrapServersProperty = "spring.kafka.bootstrap-servers"
)
public class KafkaIntegrationTest {
    
    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;
    
    @Test
    void testKafkaMessage() {
        // 测试代码
    }
}
```

---

## 五、常见问题

### 5.1 编译错误：找不到 javax 包

**问题**: 升级后出现 `package javax.servlet does not exist` 错误

**解决**: 将所有 `javax.*` 导入替换为 `jakarta.*`

### 5.2 Hibernate 方言错误

**问题**: `org.hibernate.dialect.MySQL5InnoDBDialect` 不存在

**解决**: 使用 `org.hibernate.dialect.MySQLDialect`，Hibernate 6.x 会自动检测版本

### 5.3 Spring Security antMatchers 不存在

**问题**: `antMatchers` 方法不存在

**解决**: 使用 `requestMatchers` 替代

### 5.4 MyBatis 启动失败

**问题**: MyBatis 自动配置失败

**解决**: 确保使用 mybatis-spring-boot-starter 3.0.x 版本

### 5.5 EmbeddedKafkaBroker 编译错误

**问题**: `EmbeddedKafkaBroker is abstract; cannot be instantiated`

**原因**: Spring Kafka 3.x 将 `EmbeddedKafkaBroker` 改为抽象类

**解决方案**:

1. **使用 Zookeeper 模式**（兼容旧版本）:
   ```java
   import org.springframework.kafka.test.EmbeddedKafkaZKBroker;
   
   EmbeddedKafkaBroker broker = new EmbeddedKafkaZKBroker(1, false);
   broker.kafkaPorts(9092);
   ```

2. **使用 KRaft 模式**（推荐，无需 Zookeeper）:
   ```java
   import org.springframework.kafka.test.EmbeddedKafkaKraftBroker;
   
   EmbeddedKafkaBroker broker = new EmbeddedKafkaKraftBroker(1);
   broker.kafkaPorts(9092);
   ```

3. **使用 @EmbeddedKafka 注解**（最简单）:
   ```java
   @SpringBootTest
   @EmbeddedKafka(topics = "test-topic")
   public class MyTest {
       @Autowired
       private EmbeddedKafkaBroker embeddedKafka;
   }
   ```

**注意**: `zkPort()` 方法已被移除，Zookeeper 端口由 Broker 自动管理

---

## 六、迁移工具

### 6.1 OpenRewrite

使用 OpenRewrite 自动迁移 javax → jakarta：

```xml
<plugin>
    <groupId>org.openrewrite.maven</groupId>
    <artifactId>rewrite-maven-plugin</artifactId>
    <version>5.42.0</version>
    <configuration>
        <activeRecipes>
            <recipe>org.openrewrite.java.migrate.jakarta.JavaxMigrationToJakarta</recipe>
        </activeRecipes>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>org.openrewrite.recipe</groupId>
            <artifactId>rewrite-migrate-java</artifactId>
            <version>2.26.0</version>
        </dependency>
    </dependencies>
</plugin>
```

执行迁移：

```bash
mvn rewrite:run
```

### 6.2 IntelliJ IDEA 迁移助手

1. 打开 `Refactor` → `Migrate Packages and Classes`
2. 选择 `javax to jakarta` 迁移规则
3. 预览并应用变更

---