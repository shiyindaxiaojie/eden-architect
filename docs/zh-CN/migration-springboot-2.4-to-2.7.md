# Spring Boot 2.7.x 升级指南

## 概述

本文档记录了 Eden* Architect 从 Spring Boot 2.4.13 升级到 2.7.18 的所有变更，包括依赖版本更新、配置属性变更、API 变更和代码迁移示例。

## 版本演进历史

### Spring Boot 2.4.x 系列 (2020.11 - 2022.11)

#### 2.4.13 (2022-01-20) - 当前基线版本
- 安全修复和 Bug 修复版本
- 依赖升级: Spring Framework 5.3.15, Tomcat 9.0.56
- 修复 Log4j2 安全漏洞 (CVE-2021-44228)

### Spring Boot 2.5.x 系列 (2021.05 - 2023.08)

#### 2.5.0 (2021-05-20) - 主要特性
- Java 16 支持
- Gradle 7 支持
- 改进的 Docker 镜像构建 (Paketo buildpacks)
- 新增 `spring.datasource.type` 自动配置
- 环境变量前缀支持 (`spring.config.import`)
- 新增 `GET` 请求的 Actuator 端点
- 改进的 SQL 脚本初始化机制

#### 2.5.1 - 2.5.5 (2021-06 至 2021-10)
- Bug 修复和依赖升级
- 2.5.4: 修复 Spring Data 相关问题
- 2.5.5: 安全修复

#### 2.5.6 - 2.5.8 (2021-11 至 2022-01)
- 2.5.6: 依赖升级, 修复 Hibernate 问题
- 2.5.7: Log4j2 安全修复 (CVE-2021-44228)
- 2.5.8: 进一步的 Log4j2 修复

#### 2.5.9 - 2.5.12 (2022-02 至 2022-05)
- 持续的 Bug 修复和安全更新
- 2.5.10: Spring Framework 5.3.16
- 2.5.12: 依赖升级

#### 2.5.13 - 2.5.15 (2022-06 至 2023-08)
- 2.5.13: 安全修复
- 2.5.14: 最后的功能修复
- 2.5.15: 最终维护版本

### Spring Boot 2.6.x 系列 (2021.11 - 2023.11)

#### 2.6.0 (2021-11-19) - 主要特性
- **默认禁止循环依赖** - 需要 `spring.main.allow-circular-references=true` 启用
- **PathPatternParser 成为默认路径匹配策略** - 影响 Swagger/SpringFox
- 新增 SameSite Cookie 属性支持
- 改进的 Docker 镜像构建
- 新增 `/info` 端点的 Java 运行时信息
- 支持 WebTestClient 测试 Spring MVC
- 新增健康检查组配置

#### 2.6.1 - 2.6.3 (2021-12 至 2022-01)
- 2.6.1: Bug 修复
- 2.6.2: Log4j2 安全修复 (CVE-2021-44228, CVE-2021-45046)
- 2.6.3: Log4j2 进一步修复 (CVE-2021-45105)

#### 2.6.4 - 2.6.7 (2022-02 至 2022-04)
- 2.6.4: Spring Framework 5.3.16
- 2.6.5: 依赖升级
- 2.6.6: Bug 修复
- 2.6.7: 安全修复

#### 2.6.8 - 2.6.11 (2022-05 至 2022-09)
- 2.6.8: 依赖升级
- 2.6.9: Bug 修复
- 2.6.10: 安全修复
- 2.6.11: 依赖升级

#### 2.6.12 - 2.6.15 (2022-10 至 2023-11)
- 2.6.12: 安全修复
- 2.6.13: Bug 修复
- 2.6.14: 依赖升级
- 2.6.15: 最终维护版本

### Spring Boot 2.7.x 系列 (2022.05 - 至今)

#### 2.7.0 (2022-05-19) - 主要特性
- **WebSecurityConfigurerAdapter 废弃** - 推荐使用 SecurityFilterChain
- 新增 `@AutoConfiguration` 注解
- 改进的 `@SpringBootTest` 测试切片
- GraphQL 自动配置支持
- Podman 支持 (Docker 替代)
- 新增 `spring.jpa.defer-datasource-initialization` 配置
- 改进的 Actuator 端点发现
- 支持 Java 18

#### 2.7.1 - 2.7.4 (2022-06 至 2022-09)
- 2.7.1: Bug 修复, 依赖升级
- 2.7.2: 安全修复
- 2.7.3: Bug 修复
- 2.7.4: 依赖升级

#### 2.7.5 - 2.7.8 (2022-10 至 2023-01)
- 2.7.5: Bug 修复
- 2.7.6: 安全修复 (CVE-2022-42889 Text4Shell)
- 2.7.7: 依赖升级
- 2.7.8: Bug 修复

#### 2.7.9 - 2.7.12 (2023-02 至 2023-06)
- 2.7.9: 安全修复
- 2.7.10: 依赖升级
- 2.7.11: Bug 修复
- 2.7.12: 安全修复

#### 2.7.13 - 2.7.16 (2023-07 至 2023-11)
- 2.7.13: Bug 修复
- 2.7.14: 依赖升级
- 2.7.15: 安全修复
- 2.7.16: Bug 修复

#### 2.7.17 - 2.7.18 (2023-12 至 2024-02)
- 2.7.17: 安全修复
- 2.7.18: **最终版本** - 2.7.x 系列的最后一个维护版本

### 版本升级路径建议

```
2.4.13 → 2.5.15 → 2.6.15 → 2.7.18
```

**建议分步升级**，每次升级一个主版本，确保应用稳定后再继续：

1. **2.4.x → 2.5.x**: 关注 SQL 脚本初始化变更
2. **2.5.x → 2.6.x**: 关注循环依赖和路径匹配策略变更
3. **2.6.x → 2.7.x**: 关注 Spring Security 配置方式变更

## 升级要求

| 项目 | 旧版本 | 新版本 | 说明 |
|------|--------|--------|------|
| JDK 最低版本 | 8 | 11 | 必须升级 JDK |
| Spring Boot | 2.4.13 | 2.7.18 | 核心框架升级 |
| Spring Cloud | 2020.0.1 | 2021.0.9 | 微服务框架升级 |
| Spring Cloud Alibaba | 2021.1 | 2021.0.5.0 | 阿里巴巴微服务组件 |

## 依赖版本变更清单

### Spring 生态依赖

| 依赖 | 旧版本 | 新版本 | 说明 |
|------|--------|--------|------|
| spring-boot | 2.4.13 | 2.7.18 | 核心框架升级 |
| spring-framework | 5.3.13 | 5.3.31 | 由 Spring Boot 管理 |
| spring-cloud | 2020.0.1 | 2021.0.9 | 微服务框架升级 |
| spring-cloud-alibaba | 2021.1 | 2021.0.5.0 | 阿里巴巴微服务组件 |
| spring-security | 5.4.x | 5.7.x | 由 Spring Boot 管理 |
| spring-kafka | 2.6.x | 2.7.9 | Kafka 集成 |
| spring-boot-admin | 2.4.4 | 2.7.10 | 监控管理 |

### Alibaba 生态依赖

| 依赖 | 旧版本 | 新版本 | 说明 |
|------|--------|--------|------|
| nacos-client | 2.0.4 | 2.1.2 | 服务发现与配置中心 |
| sentinel | 1.8.5 | 1.8.6 | 流量控制 |
| dubbo | 3.2.x | 3.2.14 | RPC 框架 |
| rocketmq | 4.7.1 | 4.9.7 | 消息队列 |
| rocketmq-spring-boot | 2.1.1 | 2.2.3 | RocketMQ Spring 集成 |
| druid | 1.2.14 | 1.2.20 | 数据库连接池 |
| fastjson2 | 2.0.22 | 2.0.43 | JSON 序列化 |
| easyexcel | 3.2.0 | 3.3.3 | Excel 处理 |
| transmittable-thread-local | 2.12.x | 2.13.2 | 线程上下文传递 |

### 数据库相关依赖

| 依赖 | 旧版本 | 新版本 | 说明 |
|------|--------|--------|------|
| mybatis | 3.5.x | 3.5.16 | ORM 框架 |
| mybatis-spring | 2.0.x | 2.1.2 | MyBatis Spring 集成 |
| mybatis-spring-boot | 2.1.4 | 2.3.2 | MyBatis Starter |
| mybatis-plus | 3.5.x | 3.5.7 | MyBatis 增强 |
| pagehelper | 5.x | 6.1.0 | 分页插件 |
| hibernate | 5.4.10.Final | 5.6.15.Final | JPA 实现 |
| hikaricp | 4.0.3 | 4.0.3 | 连接池（无变化） |
| shardingsphere | 5.2.1 | 5.4.1 | 分库分表 |
| dynamic-datasource | 3.5.0 | 3.6.1 | 动态数据源 |
| liquibase | 4.13.0 | 4.17.2 | 数据库版本管理 |
| mysql-connector-java | 8.0.x | 8.0.32 | MySQL 驱动 |

### 缓存与消息队列

| 依赖 | 旧版本 | 新版本 | 说明 |
|------|--------|--------|------|
| redisson | 3.x | 3.32.0 | Redis 客户端 |
| caffeine | 2.9.x | 2.9.3 | 本地缓存 |
| kafka | 2.7.x | 2.7.2 | Kafka 客户端 |

### 监控与链路追踪

| 依赖 | 旧版本 | 新版本 | 说明 |
|------|--------|--------|------|
| micrometer | 1.7.x | 1.9.17 | 指标收集 |
| spring-boot-admin | 2.4.4 | 2.7.10 | 监控管理 |
| zipkin | 2.23.x | 2.23.18 | 链路追踪 |
| brave | 5.13.x | 5.13.10 | 分布式追踪 |
| cat | 3.0.x | 3.1.0 | 美团点评 CAT |
| prometheus | 0.x | 0.16.0 | Prometheus 客户端 |

### 工具类库

| 依赖 | 旧版本 | 新版本 | 说明 |
|------|--------|--------|------|
| lombok | 1.18.18 | 1.18.30 | 代码简化 |
| mapstruct | 1.4.1.Final | 1.5.5.Final | 对象映射 |
| guava | 30.x | 32.1.2-jre | Google 工具库 |
| jackson-bom | 2.12.x | 2.14.3 | JSON 处理 |
| netty | 4.1.x | 4.1.100.Final | 网络框架 |
| log4j2 | 2.17.x | 2.20.0 | 日志框架 |
| snakeyaml | 1.28 | 1.33 | YAML 解析 |
| commons-lang3 | 3.x | 3.9 | Apache 工具库 |
| commons-io | 2.x | 2.16.1 | IO 工具库 |
| commons-codec | 1.x | 1.17.0 | 编解码工具 |
| disruptor | 3.4.x | 3.4.4 | 高性能队列 |

### 分布式组件

| 依赖 | 旧版本 | 新版本 | 说明 |
|------|--------|--------|------|
| curator | 4.3.0 | 5.4.0 | ZooKeeper 客户端 |
| zookeeper | 3.6.x | 3.8.4 | ZooKeeper |
| grpc | 1.x | 1.58.0 | RPC 框架 |
| protobuf | 3.x | 4.0.0-rc-2 | 序列化框架 |
| xxl-job | 2.3.x | 2.4.0 | 分布式任务调度 |
| quartz | 2.3.x | 2.3.2 | 定时任务 |

### 认证授权

| 依赖 | 旧版本 | 新版本 | 说明 |
|------|--------|--------|------|
| spring-security-oauth2 | 2.5.x | 2.5.2.RELEASE | OAuth2（已废弃） |
| spring-authorization-server | 0.1.x | 0.2.3 | 新授权服务器 |
| jjwt | 0.11.2 | 0.11.5 | JWT 处理 |

### 测试框架

| 依赖 | 旧版本 | 新版本 | 说明 |
|------|--------|--------|------|
| junit-jupiter | 5.7.2 | 5.9.3 | 单元测试 |
| mockito | 3.6.28 | 4.8.1 | Mock 框架 |
| testcontainers | 1.15.x | 1.18.3 | 容器化测试 |
| spock | 2.0.x | 2.3-groovy-4.0 | Groovy 测试框架 |

### 其他依赖

| 依赖 | 旧版本 | 新版本 | 说明 |
|------|--------|--------|------|
| arthas | 3.6.7 | 3.7.2 | 诊断工具 |
| dynamictp | 1.0.x | 1.2.0 | 动态线程池 |
| javers | 6.x | 6.8.1 | 对象审计 |
| cola | 4.0.x | 4.1.0 | COLA 架构 |

## 已知问题

### ShardingSphere 5.4.1 依赖问题

`eden-shardingsphere-spring-boot-starter` 模块因 `shardingsphere-jdbc-core-spring-boot-starter:5.4.1` 依赖需要阿里云私有 Maven 仓库而被排除编译。如需使用该模块，请配置阿里云 Maven 仓库：

```xml
<repository>
    <id>aliyun-releases</id>
    <url>https://packages.aliyun.com/maven/repository/2421751-release-ZmwRAc/</url>
</repository>
```

## 配置属性变更

### 废弃配置迁移

#### 1. Profile 配置

```yaml
# 旧配置 (已废弃)
spring:
  profiles: dev

# 新配置
spring:
  config:
    activate:
      on-profile: dev
```

#### 2. 多文档配置

```yaml
# 旧配置 (已废弃)
---
spring.profiles: dev
server.port: 8080

# 新配置
---
spring:
  config:
    activate:
      on-profile: dev
server:
  port: 8080
```

#### 3. 循环依赖配置

```yaml
# Spring Boot 2.6+ 默认禁止循环依赖，如需启用：
spring:
  main:
    allow-circular-references: true
```

#### 4. Bean 覆盖配置

```yaml
# Spring Boot 2.1+ 默认禁止 Bean 覆盖，如需启用：
spring:
  main:
    allow-bean-definition-overriding: true
```

#### 5. 路径匹配策略

```yaml
# Spring Boot 2.6+ 默认使用 PathPatternParser，如需使用旧策略：
spring:
  mvc:
    pathmatch:
      matching-strategy: ant_path_matcher
```

### 新增配置属性

#### 1. 优雅停机配置

```yaml
# Spring Boot 2.3+ 新增
server:
  shutdown: graceful

spring:
  lifecycle:
    timeout-per-shutdown-phase: 30s
```

#### 2. 健康检查配置

```yaml
# 新增 liveness 和 readiness 探针
management:
  endpoint:
    health:
      probes:
        enabled: true
  health:
    livenessstate:
      enabled: true
    readinessstate:
      enabled: true
```

#### 3. 日志分组配置

```yaml
# Spring Boot 2.1+ 新增日志分组
logging:
  group:
    web: org.springframework.core.codec, org.springframework.http, org.springframework.web
    sql: org.springframework.jdbc.core, org.hibernate.SQL
  level:
    web: debug
    sql: debug
```

#### 4. 配置文件导入

```yaml
# Spring Boot 2.4+ 新增配置导入
spring:
  config:
    import:
      - optional:file:./config/
      - optional:classpath:extra-config/
```

### 移除的配置属性

| 旧属性 | 替代方案 |
|--------|----------|
| `spring.profiles` | `spring.config.activate.on-profile` |
| `spring.profiles.include` | `spring.profiles.group` |
| `management.metrics.binders.jvm.enabled` | 默认启用，无需配置 |
| `spring.data.elasticsearch.client.reactive.endpoints` | 使用新的 Elasticsearch 客户端配置 |

## API 变更

### Spring Framework 变更

#### 1. WebMvcConfigurer 接口

```java
// 旧代码 - 继承 WebMvcConfigurerAdapter (已废弃)
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // ...
    }
}

// 新代码 - 直接实现 WebMvcConfigurer
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // ...
    }
}
```

#### 2. RestTemplate 构建

```java
// 旧代码
RestTemplate restTemplate = new RestTemplate();

// 新代码 - 推荐使用 RestTemplateBuilder
@Bean
public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder
        .setConnectTimeout(Duration.ofSeconds(5))
        .setReadTimeout(Duration.ofSeconds(30))
        .build();
}
```

### Spring Security 变更

#### 1. WebSecurityConfigurerAdapter 废弃

```java
// 旧代码 - 继承 WebSecurityConfigurerAdapter (已废弃)
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/public/**").permitAll()
            .anyRequest().authenticated();
    }
}

// 新代码 - 使用 SecurityFilterChain Bean
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
            .antMatchers("/public/**").permitAll()
            .anyRequest().authenticated()
        );
        return http.build();
    }
}
```

#### 2. PasswordEncoder 配置

```java
// 旧代码
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

// 新代码 - 推荐使用 DelegatingPasswordEncoder
@Bean
public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
}
```

### Spring Data 变更

#### 1. Elasticsearch 客户端

```java
// 旧代码 - 使用 RestHighLevelClient (已废弃)
@Bean
public RestHighLevelClient elasticsearchClient() {
    return new RestHighLevelClient(
        RestClient.builder(new HttpHost("localhost", 9200, "http"))
    );
}

// 新代码 - 使用 ElasticsearchClient
@Bean
public ElasticsearchClient elasticsearchClient() {
    RestClient restClient = RestClient.builder(
        new HttpHost("localhost", 9200)
    ).build();
    
    ElasticsearchTransport transport = new RestClientTransport(
        restClient, new JacksonJsonpMapper()
    );
    
    return new ElasticsearchClient(transport);
}
```

#### 2. Redis 配置

```java
// 旧代码
@Bean
public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(factory);
    return template;
}

// 新代码 - 推荐配置序列化器
@Bean
public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(factory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
    template.afterPropertiesSet();
    return template;
}
```

### MyBatis 变更

#### 1. 分页插件配置

```java
// 旧代码 - PageHelper 旧版配置
@Bean
public PageHelper pageHelper() {
    PageHelper pageHelper = new PageHelper();
    Properties properties = new Properties();
    properties.setProperty("dialect", "mysql");
    pageHelper.setProperties(properties);
    return pageHelper;
}

// 新代码 - PageHelper 新版配置
@Bean
public PageInterceptor pageInterceptor() {
    PageInterceptor pageInterceptor = new PageInterceptor();
    Properties properties = new Properties();
    properties.setProperty("helperDialect", "mysql");
    properties.setProperty("reasonable", "true");
    pageInterceptor.setProperties(properties);
    return pageInterceptor;
}
```

### Lombok 变更

#### 1. @Builder 与 @NoArgsConstructor

```java
// 旧代码 - 可能导致编译错误
@Data
@Builder
public class User {
    private String name;
}

// 新代码 - 需要显式添加构造器
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String name;
}
```

### Jackson 变更

#### 1. 日期时间序列化

```java
// 旧代码 - 可能需要手动配置
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
private Date createTime;

// 新代码 - 推荐使用 Java 8 时间 API
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
private LocalDateTime createTime;
```

#### 2. ObjectMapper 配置

```java
// 新增推荐配置
@Bean
public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    // 注册 Java 8 时间模块
    mapper.registerModule(new JavaTimeModule());
    // 禁用将日期写为时间戳
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    // 忽略未知属性
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return mapper;
}
```

## 代码迁移示例

### 示例 1: 配置类迁移

```java
// 迁移前
@Configuration
public class AppConfig extends WebMvcConfigurerAdapter {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}

// 迁移后
@Configuration
public class AppConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("*")  // 注意: allowedOrigins("*") 与 allowCredentials(true) 不兼容
            .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
```

### 示例 2: 安全配置迁移

```java
// 迁移前
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/api/public/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .httpBasic();
    }
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

// 迁移后
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .antMatchers("/api/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    @Bean
    public UserDetailsService userDetailsService() {
        // 自定义实现
        return username -> {
            // ...
        };
    }
}
```

### 示例 3: 测试类迁移

```java
// 迁移前 - JUnit 4
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    
    @Autowired
    private UserService userService;
    
    @Test
    public void testFindById() {
        // ...
    }
}

// 迁移后 - JUnit 5
@SpringBootTest
class UserServiceTest {
    
    @Autowired
    private UserService userService;
    
    @Test
    void testFindById() {
        // ...
    }
}
```

## 常见问题解答

### Q1: 升级后编译报错 "cannot find symbol: class WebMvcConfigurerAdapter"

**原因**: `WebMvcConfigurerAdapter` 在 Spring 5.0 中已废弃，在后续版本中移除。

**解决方案**: 直接实现 `WebMvcConfigurer` 接口，该接口的方法都有默认实现。

### Q2: Spring Security 配置报错

**原因**: `WebSecurityConfigurerAdapter` 在 Spring Security 5.7 中已废弃。

**解决方案**: 使用组件化配置方式，通过 `@Bean` 方法返回 `SecurityFilterChain`。

### Q3: Elasticsearch 查询报错

**原因**: `RestHighLevelClient` 已废弃，推荐使用新的 Java API Client。

**解决方案**: 迁移到 `ElasticsearchClient`，或暂时保留旧客户端但添加 `@SuppressWarnings("deprecation")`。

### Q4: 配置文件中 spring.profiles 不生效

**原因**: `spring.profiles` 属性已废弃。

**解决方案**: 使用 `spring.config.activate.on-profile` 替代。

### Q5: Mockito 测试报错

**原因**: Mockito 4.x 对 final 类和方法的 mock 行为有变化。

**解决方案**: 
1. 在 `src/test/resources/mockito-extensions/org.mockito.plugins.MockMaker` 文件中添加 `mock-maker-inline`
2. 或使用 `@MockitoSettings(strictness = Strictness.LENIENT)`

## 升级检查清单

- [ ] JDK 版本升级到 11+
- [ ] 更新 `pom.xml` 中的 Spring Boot 版本
- [ ] 更新 Spring Cloud 版本
- [ ] 更新 Spring Cloud Alibaba 版本
- [ ] 检查并更新废弃的配置属性
- [ ] 迁移 `WebMvcConfigurerAdapter` 到 `WebMvcConfigurer`
- [ ] 迁移 `WebSecurityConfigurerAdapter` 到组件化配置
- [ ] 更新测试代码到 JUnit 5
- [ ] 运行所有测试确保通过
- [ ] 检查日志中的废弃警告

## 参考资料

- [Spring Boot 2.7 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.7-Release-Notes)
- [Spring Security 5.7 Migration Guide](https://docs.spring.io/spring-security/reference/5.7/migration/index.html)
- [Spring Cloud 2021.0 Release Notes](https://github.com/spring-cloud/spring-cloud-release/wiki/Spring-Cloud-2021.0-Release-Notes)
