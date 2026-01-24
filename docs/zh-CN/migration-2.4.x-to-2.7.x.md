# Spring Boot 2.4.13 -> 2.7.18 升级指南

## 一、概述

本文档详细记录 Eden* Architect 从 Spring Boot 2.4.13 升级到 2.7.18 所有必须修改的配置和代码。

## 二、版本演进

Spring Boot 版本演进如下（🔴 不兼容 | 🟡 废弃 | 🔵 安全修复 | 🟢 Bug 修复）

| 版本 | 变更类型 | 变更内容 |
|------|----------|----------|
| 2.5.0 | 🔴 不兼容 | `spring.profiles` 废弃，改用 `spring.config.activate.on-profile` |
| 2.5.0 | 🔴 不兼容 | `spring.profiles.include` 废弃，改用 `spring.profiles.group` |
| 2.5.0 | 🔴 不兼容 | SQL 脚本初始化属性从 `spring.datasource.*` 迁移到 `spring.sql.init.*` |
| 2.5.0 | 🟡 废弃 | `spring.data.*.repositories.enabled` 废弃，改用 `spring.data.*.repositories.type` |
| 2.5.5 | 🟢 修复 | Hibernate 升级到 5.4.32，修复部分 HQL 解析问题 |
| 2.5.7 | 🔵 安全 | Log4j2 安全修复 (CVE-2021-44228) |
| 2.6.0 | 🔴 不兼容 | 默认禁止循环依赖，需配置 `spring.main.allow-circular-references=true` 启用 |
| 2.6.0 | 🔴 不兼容 | PathPatternParser 成为默认路径匹配策略，影响 Swagger/SpringFox/Knife4j |
| 2.6.0 | 🔴 不兼容 | `WebMvcConfigurerAdapter` 彻底移除（5.0 废弃，5.3 移除） |
| 2.6.0 | 🔴 不兼容 | Actuator 端点 `/info` 默认不暴露，需手动配置 |
| 2.6.2 | 🔵 安全 | Log4j2 安全修复 (CVE-2021-45046) |
| 2.6.3 | 🔵 安全 | Log4j2 安全修复 (CVE-2021-45105) |
| 2.6.6 | 🔵 安全 | Spring Framework 升级到 5.3.18，修复 RCE 漏洞 (CVE-2022-22965) |
| 2.7.0 | 🔴 不兼容 | `WebSecurityConfigurerAdapter` 废弃，改用 `SecurityFilterChain` |
| 2.7.0 | 🔴 不兼容 | `@EnableGlobalMethodSecurity` 废弃，改用 `@EnableMethodSecurity` |
| 2.7.0 | 🟡 废弃 | `spring.factories` 自动配置注册方式废弃，改用 `AutoConfiguration.imports` |
| 2.7.0 | 🟡 废弃 | Spring Security `csrf()`、`cors()` 等链式方法废弃，改用 Lambda DSL |
| 2.7.1 | 🟢 修复 | 修复 Spring Security 配置兼容性问题 |
| 2.7.3 | 🔵 安全 | SnakeYAML 安全修复 (CVE-2022-25857) |
| 2.7.5 | 🔵 安全 | 修复 Tomcat 请求走私漏洞 |
| 2.7.18 | 🟢 修复 | 最终维护版本，包含所有安全修复 |

## 三、升级清单

### 3.1 核心框架升级

| 项目 | 旧版本 | 新版本 | 必须 |
|------|--------|--------|------|
| JDK | 8 | 11+ | ✅ |
| Spring Boot | 2.4.13 | 2.7.18 | ✅ |
| Spring Framework | 5.3.13 | 5.3.31 | ✅ |
| Spring Cloud | 2020.0.x | 2021.0.9 | ✅ |
| Spring Cloud Alibaba | 2.2.7.RELEASE | 2021.0.5.0 | ✅ |
| Spring Security | 5.4.x | 5.7.x | ✅ |

> **注意**: Spring Cloud Alibaba 从 2021.0.1.0 开始采用新的版本命名规则，与 Spring Cloud 版本号对齐。`2021.0.5.0` 表示对应 Spring Cloud 2021.0.x 的第 5 个版本，实际上比旧版 `2.2.7.RELEASE` 更新。

### 3.2 数据库升级

| 依赖 | 旧版本 | 新版本 |
|------|--------|--------|
| mybatis-spring-boot | 2.1.4 | 2.3.2 |
| mybatis-plus | 3.5.x | 3.5.7 |
| pagehelper | 5.x | 6.1.0 |
| druid | 1.2.14 | 1.2.20 |
| hibernate | 5.4.10.Final | 5.6.15.Final |
| shardingsphere | 5.2.1 | 5.4.1 |
| dynamic-datasource | 3.5.0 | 3.6.1 |
| liquibase | 4.13.0 | 4.17.2 |

### 3.3 中间件升级

| 依赖 | 旧版本 | 新版本 |
|------|--------|--------|
| nacos-client | 2.0.4 | 2.1.2 |
| sentinel | 1.8.5 | 1.8.6 |
| dubbo | 3.2.x | 3.2.14 |
| redisson | 3.x | 3.32.0 |
| rocketmq | 4.7.1 | 4.9.7 |
| kafka | 2.7.x | 2.7.2 |

### 3.4 工具库升级

| 依赖 | 旧版本 | 新版本 |
|------|--------|--------|
| lombok | 1.18.18 | 1.18.30 |
| mapstruct | 1.4.1.Final | 1.5.5.Final |
| guava | 30.x | 32.1.2-jre |
| jackson | 2.12.x | 2.14.3 |
| fastjson2 | 2.0.22 | 2.0.43 |

### 3.5 测试框架升级

| 依赖 | 旧版本 | 新版本 |
|------|--------|--------|
| junit-jupiter | 5.7.2 | 5.9.3 |
| mockito | 3.6.28 | 4.8.1 |
| testcontainers | 1.15.x | 1.18.3 |

### 3.6 监控组件升级

| 依赖 | 旧版本 | 新版本 |
|------|--------|--------|
| micrometer | 1.7.x | 1.9.17 |
| spring-boot-admin | 2.4.4 | 2.7.10 |

---

## 四、升级步骤

### 4.1 配置文件变更

#### 4.1.1 Profile 激活方式 (2.5+)

```yaml
# ❌ 旧配置 - 2.4.x
spring:
  profiles: dev

# ✅ 新配置 - 2.5+
spring:
  config:
    activate:
      on-profile: dev
```

#### 4.1.2 多文档 YAML 配置 (2.5+)

```yaml
# ❌ 旧配置 - 2.4.x
---
spring.profiles: dev
server.port: 8080
---
spring.profiles: prod
server.port: 80

# ✅ 新配置 - 2.5+
---
spring:
  config:
    activate:
      on-profile: dev
server:
  port: 8080
---
spring:
  config:
    activate:
      on-profile: prod
server:
  port: 80
```

#### 4.1.3 Profile 分组 (2.5+)

```yaml
# ❌ 旧配置 - 2.4.x
spring:
  profiles:
    include: db,mq

# ✅ 新配置 - 2.5+
spring:
  profiles:
    group:
      dev: db,mq
      prod: db,mq,monitor
```

#### 4.1.4 循环依赖配置 (2.6+)

```yaml
# 2.6+ 默认禁止循环依赖，启动会报错:
# The dependencies of some of the beans in the application context form a cycle
# 临时启用（推荐重构代码消除循环依赖）
spring:
  main:
    allow-circular-references: true
```

#### 4.1.5 路径匹配策略 (2.6+)

```yaml
# 2.6+ 默认使用 PathPatternParser
# 使用 Swagger/SpringFox/Knife4j 必须配置
spring:
  mvc:
    pathmatch:
      matching-strategy: ant_path_matcher
```

#### 4.1.6 Bean 覆盖配置

```yaml
# 如需允许 Bean 定义覆盖
spring:
  main:
    allow-bean-definition-overriding: true
```

#### 4.1.7 SQL 脚本初始化 (2.5+)

```yaml
# ❌ 旧配置 - 2.4.x
spring:
  datasource:
    initialization-mode: always
    schema: classpath:schema.sql
    data: classpath:data.sql

# ✅ 新配置 - 2.5+
spring:
  sql:
    init:
      mode: always
      schema-locations: classpath:schema.sql
      data-locations: classpath:data.sql
```

#### 4.1.8 JPA 数据源初始化顺序 (2.5+)

```yaml
# 如果 JPA 实体依赖 data.sql 初始化数据
spring:
  jpa:
    defer-datasource-initialization: true
  sql:
    init:
      mode: always
```

#### 4.1.9 配置文件导入 (2.4+)

```yaml
# 新增配置导入功能
spring:
  config:
    import:
      - optional:file:./config/application.yml
      - optional:classpath:extra-config/
      - optional:configserver:http://config-server:8888
```

#### 4.1.10 优雅停机配置 (2.3+)

```yaml
server:
  shutdown: graceful

spring:
  lifecycle:
    timeout-per-shutdown-phase: 30s
```

#### 4.1.11 健康检查探针 (2.3+)

```yaml
management:
  endpoint:
    health:
      probes:
        enabled: true
      group:
        liveness:
          include: livenessState
        readiness:
          include: readinessState,db,redis
  health:
    livenessstate:
      enabled: true
    readinessstate:
      enabled: true
```

#### 4.1.12 Actuator 端点暴露 (2.6+)

```yaml
# 2.6+ /info 端点默认不暴露
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  info:
    env:
      enabled: true
```

#### 4.1.13 Redis 配置路径变更 (2.7+)

```yaml
# ❌ 旧配置 - 2.4.x
spring:
  redis:
    host: localhost
    port: 6379
    lettuce:
      pool:
        max-active: 8

# ✅ 新配置 - 2.7+ (两种写法都支持，推荐新写法)
spring:
  data:
    redis:
      host: localhost
      port: 6379
      lettuce:
        pool:
          max-active: 8
```

---

### 4.2 Spring MVC 代码变更

#### 4.2.1 WebMvcConfigurerAdapter 废弃

```java
// ❌ 旧代码 - 2.4.x
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
            .addPathPatterns("/api/**")
            .excludePathPatterns("/api/public/**");
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowCredentials(true);
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
            .addResourceLocations("classpath:/static/");
    }
}

// ✅ 新代码 - 2.7.x
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
            .addPathPatterns("/api/**")
            .excludePathPatterns("/api/public/**");
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            // 注意：allowedOrigins("*") 改为 allowedOriginPatterns("*")
            .allowedOriginPatterns("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowCredentials(true);
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
            .addResourceLocations("classpath:/static/");
    }
}
```

#### 4.2.2 CorsFilter 配置变更

```java
// ❌ 旧代码 - 2.4.x (2.7.x 会报错: allowedOrigins("*") 与 allowCredentials(true) 冲突)
@Bean
public CorsFilter corsFilter() {
    CorsConfiguration config = new CorsConfiguration();
    config.addAllowedOrigin("*");
    config.setAllowCredentials(true);
    config.addAllowedMethod("*");
    config.addAllowedHeader("*");
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
}

// ✅ 新代码 - 2.7.x
@Bean
public CorsFilter corsFilter() {
    CorsConfiguration config = new CorsConfiguration();
    // 使用 addAllowedOriginPattern 替代 addAllowedOrigin
    config.addAllowedOriginPattern("*");
    config.setAllowCredentials(true);
    config.addAllowedMethod("*");
    config.addAllowedHeader("*");
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
}
```

#### 4.2.3 RestTemplate 构建方式

```java
// ❌ 旧代码 - 2.4.x
@Bean
public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    return restTemplate;
}

// ✅ 新代码 - 2.7.x (推荐使用 Builder)
@Bean
public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder
        .setConnectTimeout(Duration.ofSeconds(5))
        .setReadTimeout(Duration.ofSeconds(30))
        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
        .build();
}
```

---

### 4.3 Spring Security 代码变更 (2.7+)

#### 4.3.1 基础安全配置

```java
// ❌ 旧代码 - 2.4.x
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/api/public/**").permitAll()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            .and()
            .httpBasic();
    }
}

// ✅ 新代码 - 2.7.x
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
            )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
```

#### 4.3.2 完整 JWT 安全配置

```java
// ❌ 旧代码 - 2.4.x
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private JwtAuthenticationFilter jwtFilter;
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint)
            .and()
            .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

// ✅ 新代码 - 2.7.x
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationFilter jwtFilter;
    
    @Autowired
    private AuthenticationEntryPoint authEntryPoint;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(authEntryPoint))
            .authorizeHttpRequests(auth -> auth
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
```

#### 4.3.3 多 HttpSecurity 配置

```java
// ❌ 旧代码 - 2.4.x
@Configuration
@EnableWebSecurity
public class MultiSecurityConfig {
    
    @Configuration
    @Order(1)
    public static class ApiSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/api/**")
                .authorizeRequests()
                    .anyRequest().authenticated()
                .and()
                .httpBasic();
        }
    }
    
    @Configuration
    @Order(2)
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .anyRequest().permitAll()
                .and()
                .formLogin();
        }
    }
}

// ✅ 新代码 - 2.7.x
@Configuration
@EnableWebSecurity
public class MultiSecurityConfig {
    
    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
            .antMatcher("/api/**")
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
    
    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            .formLogin(Customizer.withDefaults());
        return http.build();
    }
}
```

#### 4.3.4 方法级安全配置

```java
// ❌ 旧代码 - 2.4.x
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    // ...
}

// ✅ 新代码 - 2.7.x
@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class MethodSecurityConfig {
    // 无需继承，直接使用注解即可
}
```

#### 4.3.5 Security Lambda DSL 完整对照

```java
// ❌ 旧代码 - 链式调用 (2.4.x)
http.csrf().disable();
http.cors();
http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
http.exceptionHandling().authenticationEntryPoint(entryPoint);
http.headers().frameOptions().disable();
http.formLogin().loginPage("/login").permitAll();
http.logout().logoutUrl("/logout").logoutSuccessUrl("/");
http.rememberMe().key("uniqueKey").tokenValiditySeconds(86400);
http.oauth2Login().loginPage("/oauth2/login");

// ✅ 新代码 - Lambda DSL (2.7.x)
http.csrf(csrf -> csrf.disable());
http.cors(cors -> cors.configurationSource(corsSource));
http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
http.exceptionHandling(ex -> ex.authenticationEntryPoint(entryPoint));
http.headers(headers -> headers.frameOptions(frame -> frame.disable()));
http.formLogin(form -> form.loginPage("/login").permitAll());
http.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/"));
http.rememberMe(remember -> remember.key("uniqueKey").tokenValiditySeconds(86400));
http.oauth2Login(oauth2 -> oauth2.loginPage("/oauth2/login"));
```

---

### 4.4 Spring Data 代码变更

#### 4.4.1 Elasticsearch RestHighLevelClient 废弃

```java
// ❌ 旧代码 - 2.4.x
@Configuration
public class ElasticsearchConfig {
    
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        return new RestHighLevelClient(
            RestClient.builder(
                new HttpHost("localhost", 9200, "http")
            )
        );
    }
}

// ✅ 新代码 - 2.7.x
@Configuration
public class ElasticsearchConfig {
    
    @Bean
    public ElasticsearchClient elasticsearchClient() {
        RestClient restClient = RestClient.builder(
            new HttpHost("localhost", 9200)
        ).build();
        
        ElasticsearchTransport transport = new RestClientTransport(
            restClient, 
            new JacksonJsonpMapper()
        );
        
        return new ElasticsearchClient(transport);
    }
}
```

#### 4.4.2 Elasticsearch Document 注解

```java
// ❌ 旧代码 - 2.4.x
@Document(indexName = "user", type = "_doc")
public class UserDocument {
    @Id
    private String id;
    
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String name;
}

// ✅ 新代码 - 2.7.x (移除 type 属性，ES 7.x 已废弃 type)
@Document(indexName = "user")
public class UserDocument {
    @Id
    private String id;
    
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String name;
}
```

#### 4.4.3 Redis 配置变更

```java
// ❌ 旧代码 - 2.4.x (默认序列化器可能导致乱码)
@Configuration
public class RedisConfig {
    
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        return template;
    }
}

// ✅ 新代码 - 2.7.x (推荐显式配置序列化器)
@Configuration
public class RedisConfig {
    
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        
        // Key 序列化
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        
        // Value 序列化
        GenericJackson2JsonRedisSerializer jsonSerializer = 
            new GenericJackson2JsonRedisSerializer();
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);
        
        template.afterPropertiesSet();
        return template;
    }
}
```

#### 4.4.4 JPA Repository 变更

```java
// ❌ 旧代码 - 2.4.x
public interface UserRepository extends JpaRepository<User, Long> {
    
    @Query("SELECT u FROM User u WHERE u.status = ?1")
    List<User> findByStatus(Integer status);
    
    // getOne 已废弃
    default User getById(Long id) {
        return getOne(id);
    }
}

// ✅ 新代码 - 2.7.x
public interface UserRepository extends JpaRepository<User, Long> {
    
    @Query("SELECT u FROM User u WHERE u.status = ?1")
    List<User> findByStatus(Integer status);
    
    // 使用 getReferenceById 替代 getOne
    default User getById(Long id) {
        return getReferenceById(id);
    }
}
```

---

### 4.5 MyBatis 代码变更

#### 4.5.1 PageHelper 分页插件

```java
// ❌ 旧代码 - PageHelper 5.x
@Configuration
public class MyBatisConfig {
    
    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("dialect", "mysql");
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        pageHelper.setProperties(properties);
        return pageHelper;
    }
}

// ✅ 新代码 - PageHelper 6.x
@Configuration
public class MyBatisConfig {
    
    @Bean
    public PageInterceptor pageInterceptor() {
        PageInterceptor interceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("params", "count=countSql");
        interceptor.setProperties(properties);
        return interceptor;
    }
}
```

---

### 4.6 测试代码变更

#### 4.6.1 JUnit 4 → JUnit 5

```java
// ❌ 旧代码 - JUnit 4
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    
    @Autowired
    private UserService userService;
    
    @BeforeClass
    public static void beforeAll() { }
    
    @Before
    public void setUp() { }
    
    @Test
    public void testFindById() {
        User user = userService.findById(1L);
        assertNotNull(user);
        assertEquals("test", user.getName());
    }
    
    @Test(expected = UserNotFoundException.class)
    public void testFindByIdNotFound() {
        userService.findById(999L);
    }
    
    @Test(timeout = 1000)
    public void testTimeout() { }
    
    @Ignore("暂时跳过")
    @Test
    public void testIgnored() { }
    
    @After
    public void tearDown() { }
    
    @AfterClass
    public static void afterAll() { }
}

// ✅ 新代码 - JUnit 5
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    
    @Autowired
    private UserService userService;
    
    @BeforeAll
    static void beforeAll() { }
    
    @BeforeEach
    void setUp() { }
    
    @Test
    void testFindById() {
        User user = userService.findById(1L);
        assertNotNull(user);
        assertEquals("test", user.getName());
    }
    
    @Test
    void testFindByIdNotFound() {
        assertThrows(UserNotFoundException.class, () -> {
            userService.findById(999L);
        });
    }
    
    @Test
    @Timeout(1)
    void testTimeout() { }
    
    @Disabled("暂时跳过")
    @Test
    void testIgnored() { }
    
    @AfterEach
    void tearDown() { }
    
    @AfterAll
    static void afterAll() { }
}
```

#### 4.6.2 Mockito 变更

```java
// ❌ 旧代码 - Mockito 3.x + JUnit 4
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserServiceImpl userService;
    
    @Test
    public void testFindById() {
        when(userRepository.findById(1L))
            .thenReturn(Optional.of(new User(1L, "test")));
        
        User user = userService.findById(1L);
        
        assertNotNull(user);
        verify(userRepository, times(1)).findById(1L);
    }
}

// ✅ 新代码 - Mockito 4.x + JUnit 5
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserServiceImpl userService;
    
    @Test
    void testFindById() {
        when(userRepository.findById(1L))
            .thenReturn(Optional.of(new User(1L, "test")));
        
        User user = userService.findById(1L);
        
        assertNotNull(user);
        verify(userRepository, times(1)).findById(1L);
    }
}
```

#### 4.6.3 Mockito Mock Final 类

```java
// Mockito 4.x 默认不能 mock final 类/方法
// 需要创建文件: src/test/resources/mockito-extensions/org.mockito.plugins.MockMaker
// 文件内容: mock-maker-inline

// 或者使用注解配置
@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class MyTest {
    
    @Mock
    private FinalClass finalClass;  // 现在可以 mock final 类
    
    @Test
    void test() {
        when(finalClass.finalMethod()).thenReturn("mocked");
    }
}
```

#### 4.6.4 WebMvcTest 变更

```java
// ❌ 旧代码 - 2.4.x
@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    public void testGetUser() throws Exception {
        when(userService.findById(1L)).thenReturn(new User(1L, "test"));
        
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("test"));
    }
}

// ✅ 新代码 - 2.7.x
@WebMvcTest(UserController.class)
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    void testGetUser() throws Exception {
        when(userService.findById(1L)).thenReturn(new User(1L, "test"));
        
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("test"));
    }
}
```

---

### 4.7 Lombok 代码变更

#### 4.7.1 @Builder 与构造器

```java
// ❌ 旧代码 - 2.4.x (2.7.x 可能编译错误)
@Data
@Builder
public class User {
    private Long id;
    private String name;
    private String email;
}

// ✅ 新代码 - 2.7.x (需要显式添加构造器)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
}
```

#### 4.7.2 @Builder.Default

```java
// ❌ 旧代码 - 默认值可能不生效
@Data
@Builder
public class Order {
    private Long id;
    private Integer status = 0;  // Builder 模式下默认值不生效
    private List<OrderItem> items = new ArrayList<>();
}

// ✅ 新代码 - 使用 @Builder.Default
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    
    @Builder.Default
    private Integer status = 0;
    
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();
}
```

---

### 4.8 Jackson 代码变更

#### 4.8.1 日期时间序列化

```java
// ❌ 旧代码 - 使用 java.util.Date
public class User {
    private Long id;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}

// ✅ 新代码 - 使用 Java 8 时间 API
public class User {
    private Long id;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
```

#### 4.8.2 ObjectMapper 配置

```java
// ❌ 旧代码 - 2.4.x
@Bean
public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    return mapper;
}

// ✅ 新代码 - 2.7.x
@Bean
public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    
    // 注册 Java 8 时间模块
    mapper.registerModule(new JavaTimeModule());
    
    // 禁用将日期写为时间戳
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    
    // 忽略未知属性
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    
    // 空对象不报错
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    
    // 允许单引号
    mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    
    return mapper;
}
```

---

### 4.9 自动配置变更

#### 4.9.1 自动配置注解 (2.7+)

```java
// ❌ 旧代码 - 2.4.x
@Configuration
@ConditionalOnClass(DataSource.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(MyProperties.class)
public class MyAutoConfiguration {
    // ...
}

// ✅ 新代码 - 2.7.x (推荐使用新注解)
@AutoConfiguration(after = DataSourceAutoConfiguration.class)
@ConditionalOnClass(DataSource.class)
@EnableConfigurationProperties(MyProperties.class)
public class MyAutoConfiguration {
    // ...
}
```

#### 4.9.2 自动配置注册方式 (2.7+)

```properties
# ❌ 旧方式 - META-INF/spring.factories (2.7+ 废弃)
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  com.example.config.MyAutoConfiguration,\
  com.example.config.AnotherAutoConfiguration

# ✅ 新方式 - META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
com.example.config.MyAutoConfiguration
com.example.config.AnotherAutoConfiguration
```

---

### 4.10 日志配置变更

#### 4.10.1 日志分组 (2.5+)

```yaml
# 新增日志分组功能，方便统一管理
logging:
  group:
    web: org.springframework.core.codec,org.springframework.http,org.springframework.web
    sql: org.springframework.jdbc.core,org.hibernate.SQL,org.jooq.tools.LoggerListener
    security: org.springframework.security
    mybatis: org.mybatis,com.baomidou.mybatisplus
  level:
    web: debug
    sql: debug
    security: debug
    mybatis: debug
```

#### 4.11 POM 结构优化 (Maven 3.9+)

在升级过程中，部分 POM 文件的结构可能触发 Maven 3.9+ 的严格校验，导致构建失败或产生 `malformed project` 警告。

##### 4.11.1 依赖作用域规范

`scope` 不允许使用 `optional` 字符串，应使用 `<optional>true</optional>` 标签。

```xml
<!-- ❌ 错误做法 -->
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <scope>optional</scope>
</dependency>

<!-- ✅ 正确做法 -->
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <optional>true</optional>
</dependency>
```

##### 4.11.2 重复依赖清理

确保同一个 POM 文件中没有重复声明同一个依赖（甚至不同版本）。

```xml
<!-- ❌ 错误做法：在不同地方重复声明 -->
<dependency>
    <groupId>io.github.shiyindaxiaojie</groupId>
    <artifactId>eden-spring-data</artifactId>
</dependency>
<!-- ... -->
<dependency>
    <groupId>io.github.shiyindaxiaojie</groupId>
    <artifactId>eden-spring-data</artifactId>
</dependency>
```

##### 4.11.3 插件版本管理

所有在子模块中使用的插件，推荐在 `eden-parent` 的 `pluginManagement` 中进行统一定义，避免子模块因缺失版本号而报错。

```xml
<!-- eden-parent/pom.xml -->
<pluginManagement>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-gpg-plugin</artifactId>
            <version>${maven-gpg-plugin.version}</version>
        </plugin>
    </plugins>
</pluginManagement>
```

---

## 五、常见问题解答

### Q1: 编译报错 "cannot find symbol: class WebMvcConfigurerAdapter"

**原因**: `WebMvcConfigurerAdapter` 在 Spring 5.0 废弃，5.3 彻底移除。

**解决**: 改为实现 `WebMvcConfigurer` 接口，该接口所有方法都有默认实现，无需全部重写。

### Q2: 编译报错 "cannot find symbol: class WebSecurityConfigurerAdapter"

**原因**: Spring Security 5.7 废弃该类。

**解决**: 使用 `@Bean` 方法返回 `SecurityFilterChain`，参考 4.3 节代码示例。

### Q3: 启动报错 "The dependencies of some of the beans in the application context form a cycle"

**原因**: 2.6+ 默认禁止循环依赖。

**解决**:
1. 推荐：重构代码，使用 `@Lazy` 注解或提取公共依赖消除循环
2. 临时：配置 `spring.main.allow-circular-references=true`

### Q4: Swagger/SpringFox/Knife4j 页面 404

**原因**: 2.6+ 默认使用 PathPatternParser，与 SpringFox 不兼容。

**解决**: 配置 `spring.mvc.pathmatch.matching-strategy=ant_path_matcher`

### Q5: 配置文件中 spring.profiles 不生效

**原因**: 2.5+ 废弃 `spring.profiles`。

**解决**: 使用 `spring.config.activate.on-profile` 替代，参考 4.1.1 节。

### Q6: Mockito 报错 "Cannot mock/spy class ... Mockito cannot mock this class"

**原因**: Mockito 4.x 默认不能 mock final 类/方法。

**解决**:
1. 创建 `src/test/resources/mockito-extensions/org.mockito.plugins.MockMaker`
2. 文件内容填写 `mock-maker-inline`

### Q7: Elasticsearch RestHighLevelClient 废弃警告

**原因**: Elasticsearch 7.15+ 废弃 RestHighLevelClient。

**解决**:
1. 迁移到新的 `ElasticsearchClient`，参考 4.4.1 节
2. 或暂时添加 `@SuppressWarnings("deprecation")`

### Q8: allowedOrigins("*") 与 allowCredentials(true) 冲突报错

**原因**: 2.7.x 安全策略不允许同时使用通配符和凭证。

**解决**: 使用 `allowedOriginPatterns("*")` 替代 `allowedOrigins("*")`，参考 4.2.2 节。

### Q9: PageHelper 分页不生效或报错

**原因**: PageHelper 6.x API 变更，`PageHelper` 类改为 `PageInterceptor`。

**解决**: 使用 `PageInterceptor` 替代 `PageHelper`，参考 4.5.1 节。

### Q10: @Builder 与 @NoArgsConstructor 编译错误

**原因**: Lombok 版本升级后，@Builder 需要配合构造器注解使用。

**解决**: 同时添加 `@NoArgsConstructor` 和 `@AllArgsConstructor`，参考 4.7.1 节。

### Q11: AuthenticationManager 无法注入

**原因**: 2.7.x 移除了 `authenticationManagerBean()` 方法。

**解决**: 通过 `AuthenticationConfiguration` 获取：
```java
@Bean
public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
}
```

### Q12: Spring Security csrf().disable() 等方法废弃警告

**原因**: 2.7.x 推荐使用 Lambda DSL。

**解决**: 参考 4.3.5 节 Lambda DSL 完整对照表。

### Q13: 多文档 YAML 配置不生效

**原因**: 2.5+ 废弃 `spring.profiles`。

**解决**: 使用 `spring.config.activate.on-profile`，参考 4.1.2 节。

### Q14: ShardingSphere 依赖下载失败

**原因**: ShardingSphere 5.4.1 需要阿里云私有仓库。

**解决**: 配置阿里云 Maven 仓库：
```xml
<repository>
    <id>aliyun-releases</id>
    <url>https://packages.aliyun.com/maven/repository/2421751-release-ZmwRAc/</url>
</repository>
```

### Q15: Spring Boot Admin 连接失败

**原因**: Admin Server 和 Client 版本不匹配。

**解决**: 都升级到 2.7.10，保持版本一致。

### Q16: JUnit 4 @RunWith 注解不生效

**原因**: 2.7.x 默认使用 JUnit 5，不需要 `@RunWith`。

**解决**: 移除 `@RunWith(SpringRunner.class)`，JUnit 5 自动集成。

### Q17: @Test(expected = Exception.class) 编译错误

**原因**: JUnit 5 移除了 `expected` 属性。

**解决**: 使用 `assertThrows`：
```java
@Test
void testException() {
    assertThrows(UserNotFoundException.class, () -> {
        userService.findById(999L);
    });
}
```

### Q18: spring.factories 自动配置不生效

**原因**: 2.7+ 推荐使用新的注册方式。

**解决**: 创建 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 文件，参考 4.9.2 节。

### Q19: Actuator /info 端点返回空

**原因**: 2.6+ `/info` 端点默认不暴露环境信息。

**解决**: 配置 `management.info.env.enabled=true`，参考 4.1.12 节。

### Q20: JPA getOne() 方法废弃警告

**原因**: `getOne()` 在 Spring Data JPA 2.5 废弃。

**解决**: 使用 `getReferenceById()` 替代，参考 4.4.4 节。

---

## 六、参考资料

- [Spring Boot 2.5 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.5-Release-Notes)
- [Spring Boot 2.6 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.6-Release-Notes)
- [Spring Boot 2.7 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.7-Release-Notes)
- [Spring Security 5.7 Migration Guide](https://docs.spring.io/spring-security/reference/5.7/migration/index.html)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
