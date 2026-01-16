# Spring Boot 2.4.13 -> 2.7.18 Migration Guide

## 1. Overview

This document details all configuration and code changes required for upgrading Eden* Architect from Spring Boot 2.4.13 to 2.7.18.

## 2. Version Evolution

Spring Boot version evolution (🔴 Breaking | 🟡 Deprecated | 🔵 Security | 🟢 Bug Fix)

| Version | Type | Change |
|---------|------|--------|
| 2.5.0 | 🔴 Breaking | `spring.profiles` deprecated, use `spring.config.activate.on-profile` |
| 2.5.0 | 🔴 Breaking | `spring.profiles.include` deprecated, use `spring.profiles.group` |
| 2.5.0 | 🔴 Breaking | SQL script init properties moved from `spring.datasource.*` to `spring.sql.init.*` |
| 2.5.0 | 🟡 Deprecated | `spring.data.*.repositories.enabled` deprecated, use `spring.data.*.repositories.type` |
| 2.5.5 | 🟢 Bug Fix | Hibernate upgraded to 5.4.32, fixed HQL parsing issues |
| 2.5.7 | 🔵 Security | Log4j2 security fix (CVE-2021-44228) |
| 2.6.0 | 🔴 Breaking | Circular dependencies disabled by default, use `spring.main.allow-circular-references=true` |
| 2.6.0 | 🔴 Breaking | PathPatternParser becomes default, affects Swagger/SpringFox/Knife4j |
| 2.6.0 | 🔴 Breaking | `WebMvcConfigurerAdapter` removed (deprecated in 5.0, removed in 5.3) |
| 2.6.0 | 🔴 Breaking | Actuator `/info` endpoint not exposed by default |
| 2.6.2 | 🔵 Security | Log4j2 security fix (CVE-2021-45046) |
| 2.6.3 | 🔵 Security | Log4j2 security fix (CVE-2021-45105) |
| 2.6.6 | 🔵 Security | Spring Framework 5.3.18, RCE fix (CVE-2022-22965) |
| 2.7.0 | 🔴 Breaking | `WebSecurityConfigurerAdapter` deprecated, use `SecurityFilterChain` |
| 2.7.0 | 🔴 Breaking | `@EnableGlobalMethodSecurity` deprecated, use `@EnableMethodSecurity` |
| 2.7.0 | 🟡 Deprecated | `spring.factories` auto-config deprecated, use `AutoConfiguration.imports` |
| 2.7.0 | 🟡 Deprecated | Spring Security `csrf()`, `cors()` chain methods deprecated, use Lambda DSL |
| 2.7.1 | 🟢 Bug Fix | Spring Security configuration compatibility fix |
| 2.7.3 | 🔵 Security | SnakeYAML security fix (CVE-2022-25857) |
| 2.7.5 | 🔵 Security | Tomcat request smuggling fix |
| 2.7.18 | 🟢 Bug Fix | Final maintenance release with all security fixes |

## 3. Upgrade Checklist

### 3.1 Core Framework

| Component | Old Version | New Version | Required |
|-----------|-------------|-------------|----------|
| JDK | 8 | 11+ | ✅ |
| Spring Boot | 2.4.13 | 2.7.18 | ✅ |
| Spring Framework | 5.3.13 | 5.3.31 | ✅ |
| Spring Cloud | 2020.0.x | 2021.0.9 | ✅ |
| Spring Cloud Alibaba | 2.2.7.RELEASE | 2021.0.5.0 | ✅ |
| Spring Security | 5.4.x | 5.7.x | ✅ |

> **Note**: Spring Cloud Alibaba adopted a new versioning scheme starting from 2021.0.1.0, aligned with Spring Cloud versions. `2021.0.5.0` means the 5th release for Spring Cloud 2021.0.x, which is actually newer than `2.2.7.RELEASE`.

### 3.2 Database

| Dependency | Old Version | New Version |
|------------|-------------|-------------|
| mybatis-spring-boot | 2.1.4 | 2.3.2 |
| mybatis-plus | 3.5.x | 3.5.7 |
| pagehelper | 5.x | 6.1.0 |
| druid | 1.2.14 | 1.2.20 |
| hibernate | 5.4.10.Final | 5.6.15.Final |
| shardingsphere | 5.2.1 | 5.4.1 |
| dynamic-datasource | 3.5.0 | 3.6.1 |
| liquibase | 4.13.0 | 4.17.2 |

### 3.3 Middleware

| Dependency | Old Version | New Version |
|------------|-------------|-------------|
| nacos-client | 2.0.4 | 2.1.2 |
| sentinel | 1.8.5 | 1.8.6 |
| dubbo | 3.2.x | 3.2.14 |
| redisson | 3.x | 3.32.0 |
| rocketmq | 4.7.1 | 4.9.7 |
| kafka | 2.7.x | 2.7.2 |

### 3.4 Utilities

| Dependency | Old Version | New Version |
|------------|-------------|-------------|
| lombok | 1.18.18 | 1.18.30 |
| mapstruct | 1.4.1.Final | 1.5.5.Final |
| guava | 30.x | 32.1.2-jre |
| jackson | 2.12.x | 2.14.3 |
| fastjson2 | 2.0.22 | 2.0.43 |

### 3.5 Testing

| Dependency | Old Version | New Version |
|------------|-------------|-------------|
| junit-jupiter | 5.7.2 | 5.9.3 |
| mockito | 3.6.28 | 4.8.1 |
| testcontainers | 1.15.x | 1.18.3 |

### 3.6 Monitoring

| Dependency | Old Version | New Version |
|------------|-------------|-------------|
| micrometer | 1.7.x | 1.9.17 |
| spring-boot-admin | 2.4.4 | 2.7.10 |

---

## 4. Migration Steps

### 4.1 Configuration Changes

#### 4.1.1 Profile Activation (2.5+)

```yaml
# ❌ Old - 2.4.x
spring:
  profiles: dev

# ✅ New - 2.5+
spring:
  config:
    activate:
      on-profile: dev
```

#### 4.1.2 Multi-document YAML (2.5+)

```yaml
# ❌ Old - 2.4.x
---
spring.profiles: dev
server.port: 8080
---
spring.profiles: prod
server.port: 80

# ✅ New - 2.5+
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

#### 4.1.3 Profile Groups (2.5+)

```yaml
# ❌ Old - 2.4.x
spring:
  profiles:
    include: db,mq

# ✅ New - 2.5+
spring:
  profiles:
    group:
      dev: db,mq
      prod: db,mq,monitor
```

#### 4.1.4 Circular Dependencies (2.6+)

```yaml
# 2.6+ disables circular dependencies by default
# Error: The dependencies of some of the beans in the application context form a cycle
# To enable temporarily (refactoring recommended):
spring:
  main:
    allow-circular-references: true
```

#### 4.1.5 Path Matching Strategy (2.6+)

```yaml
# 2.6+ uses PathPatternParser by default
# Required for Swagger/SpringFox/Knife4j
spring:
  mvc:
    pathmatch:
      matching-strategy: ant_path_matcher
```

#### 4.1.6 Bean Override

```yaml
# To allow bean definition overriding
spring:
  main:
    allow-bean-definition-overriding: true
```

#### 4.1.7 SQL Script Initialization (2.5+)

```yaml
# ❌ Old - 2.4.x
spring:
  datasource:
    initialization-mode: always
    schema: classpath:schema.sql
    data: classpath:data.sql

# ✅ New - 2.5+
spring:
  sql:
    init:
      mode: always
      schema-locations: classpath:schema.sql
      data-locations: classpath:data.sql
```

#### 4.1.8 JPA Datasource Initialization Order (2.5+)

```yaml
# If JPA entities depend on data.sql initialization
spring:
  jpa:
    defer-datasource-initialization: true
  sql:
    init:
      mode: always
```

#### 4.1.9 Config Import (2.4+)

```yaml
spring:
  config:
    import:
      - optional:file:./config/application.yml
      - optional:classpath:extra-config/
      - optional:configserver:http://config-server:8888
```

#### 4.1.10 Graceful Shutdown (2.3+)

```yaml
server:
  shutdown: graceful

spring:
  lifecycle:
    timeout-per-shutdown-phase: 30s
```

#### 4.1.11 Health Probes (2.3+)

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

#### 4.1.12 Actuator Endpoint Exposure (2.6+)

```yaml
# 2.6+ /info endpoint not exposed by default
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  info:
    env:
      enabled: true
```

#### 4.1.13 Redis Configuration Path (2.7+)

```yaml
# ❌ Old - 2.4.x
spring:
  redis:
    host: localhost
    port: 6379
    lettuce:
      pool:
        max-active: 8

# ✅ New - 2.7+ (both supported, new path recommended)
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

### 4.2 Spring MVC Code Changes

#### 4.2.1 WebMvcConfigurerAdapter Deprecated

```java
// ❌ Old - 2.4.x
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
}

// ✅ New - 2.7.x
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
            // Note: allowedOrigins("*") changed to allowedOriginPatterns("*")
            .allowedOriginPatterns("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowCredentials(true);
    }
}
```

#### 4.2.2 CorsFilter Configuration

```java
// ❌ Old - 2.4.x (2.7.x error: allowedOrigins("*") conflicts with allowCredentials(true))
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

// ✅ New - 2.7.x
@Bean
public CorsFilter corsFilter() {
    CorsConfiguration config = new CorsConfiguration();
    // Use addAllowedOriginPattern instead of addAllowedOrigin
    config.addAllowedOriginPattern("*");
    config.setAllowCredentials(true);
    config.addAllowedMethod("*");
    config.addAllowedHeader("*");
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
}
```

#### 4.2.3 RestTemplate Builder

```java
// ❌ Old - 2.4.x
@Bean
public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    return restTemplate;
}

// ✅ New - 2.7.x (Builder recommended)
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

### 4.3 Spring Security Code Changes (2.7+)

#### 4.3.1 Basic Security Configuration

```java
// ❌ Old - 2.4.x
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

// ✅ New - 2.7.x
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

#### 4.3.2 Complete JWT Security Configuration

```java
// ❌ Old - 2.4.x
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
            .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
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

// ✅ New - 2.7.x
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationFilter jwtFilter;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .antMatchers("/auth/**").permitAll()
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
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
```

#### 4.3.3 Method Security Configuration

```java
// ❌ Old - 2.4.x
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
}

// ✅ New - 2.7.x
@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MethodSecurityConfig {
    // No inheritance needed
}
```

#### 4.3.4 Security Lambda DSL Reference

```java
// ❌ Old - Chain calls (2.4.x)
http.csrf().disable();
http.cors();
http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
http.headers().frameOptions().disable();
http.formLogin().loginPage("/login").permitAll();
http.logout().logoutUrl("/logout").logoutSuccessUrl("/");

// ✅ New - Lambda DSL (2.7.x)
http.csrf(csrf -> csrf.disable());
http.cors(cors -> cors.configurationSource(corsSource));
http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
http.headers(headers -> headers.frameOptions(frame -> frame.disable()));
http.formLogin(form -> form.loginPage("/login").permitAll());
http.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/"));
```

---

### 4.4 Spring Data Code Changes

#### 4.4.1 Elasticsearch RestHighLevelClient Deprecated

```java
// ❌ Old - 2.4.x
@Configuration
public class ElasticsearchConfig {
    
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        return new RestHighLevelClient(
            RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
    }
}

// ✅ New - 2.7.x
@Configuration
public class ElasticsearchConfig {
    
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
}
```

#### 4.4.2 Elasticsearch Document Annotation

```java
// ❌ Old - 2.4.x
@Document(indexName = "user", type = "_doc")
public class UserDocument {
    @Id
    private String id;
}

// ✅ New - 2.7.x (remove type attribute, ES 7.x deprecated types)
@Document(indexName = "user")
public class UserDocument {
    @Id
    private String id;
}
```

#### 4.4.3 JPA Repository Changes

```java
// ❌ Old - 2.4.x
public interface UserRepository extends JpaRepository<User, Long> {
    // getOne deprecated
    default User getById(Long id) {
        return getOne(id);
    }
}

// ✅ New - 2.7.x
public interface UserRepository extends JpaRepository<User, Long> {
    // Use getReferenceById instead of getOne
    default User getById(Long id) {
        return getReferenceById(id);
    }
}
```

---

### 4.5 MyBatis Code Changes

#### 4.5.1 PageHelper Plugin

```java
// ❌ Old - PageHelper 5.x
@Configuration
public class MyBatisConfig {
    
    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("dialect", "mysql");
        pageHelper.setProperties(properties);
        return pageHelper;
    }
}

// ✅ New - PageHelper 6.x
@Configuration
public class MyBatisConfig {
    
    @Bean
    public PageInterceptor pageInterceptor() {
        PageInterceptor interceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        properties.setProperty("reasonable", "true");
        interceptor.setProperties(properties);
        return interceptor;
    }
}
```

---

### 4.6 Test Code Changes

#### 4.6.1 JUnit 4 → JUnit 5

```java
// ❌ Old - JUnit 4
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    
    @Before
    public void setUp() { }
    
    @Test
    public void testFindById() {
        assertNotNull(user);
    }
    
    @Test(expected = UserNotFoundException.class)
    public void testFindByIdNotFound() {
        userService.findById(999L);
    }
}

// ✅ New - JUnit 5
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    
    @BeforeEach
    void setUp() { }
    
    @Test
    void testFindById() {
        assertNotNull(user);
    }
    
    @Test
    void testFindByIdNotFound() {
        assertThrows(UserNotFoundException.class, () -> {
            userService.findById(999L);
        });
    }
}
```

#### 4.6.2 Mockito Changes

```java
// ❌ Old - Mockito 3.x + JUnit 4
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
}

// ✅ New - Mockito 4.x + JUnit 5
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
}
```

#### 4.6.3 Mockito Mock Final Classes

```java
// Mockito 4.x cannot mock final classes by default
// Create file: src/test/resources/mockito-extensions/org.mockito.plugins.MockMaker
// Content: mock-maker-inline
```

---

### 4.7 Lombok Code Changes

#### 4.7.1 @Builder with Constructors

```java
// ❌ Old - 2.4.x (may cause compile error in 2.7.x)
@Data
@Builder
public class User {
    private Long id;
    private String name;
}

// ✅ New - 2.7.x (explicit constructors required)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
}
```

---

## 5. FAQ

### Q1: Compile error "cannot find symbol: class WebMvcConfigurerAdapter"

**Cause**: `WebMvcConfigurerAdapter` deprecated in Spring 5.0, removed in 5.3.

**Solution**: Implement `WebMvcConfigurer` interface directly.

### Q2: Compile error "cannot find symbol: class WebSecurityConfigurerAdapter"

**Cause**: Spring Security 5.7 deprecated this class.

**Solution**: Use `@Bean` method returning `SecurityFilterChain`.

### Q3: Startup error "The dependencies of some of the beans form a cycle"

**Cause**: 2.6+ disables circular dependencies by default.

**Solution**:
1. Recommended: Refactor code, use `@Lazy` to eliminate cycles
2. Temporary: Set `spring.main.allow-circular-references=true`

### Q4: Swagger/SpringFox/Knife4j returns 404

**Cause**: 2.6+ uses PathPatternParser by default, incompatible with SpringFox.

**Solution**: Set `spring.mvc.pathmatch.matching-strategy=ant_path_matcher`

### Q5: spring.profiles configuration not working

**Cause**: 2.5+ deprecated `spring.profiles`.

**Solution**: Use `spring.config.activate.on-profile` instead.

### Q6: Mockito error "Cannot mock/spy class"

**Cause**: Mockito 4.x cannot mock final classes by default.

**Solution**: Create `src/test/resources/mockito-extensions/org.mockito.plugins.MockMaker` with content `mock-maker-inline`

### Q7: allowedOrigins("*") conflicts with allowCredentials(true)

**Cause**: 2.7.x security policy disallows wildcard with credentials.

**Solution**: Use `allowedOriginPatterns("*")` instead of `allowedOrigins("*")`.

### Q8: PageHelper pagination not working

**Cause**: PageHelper 6.x API changed.

**Solution**: Use `PageInterceptor` instead of `PageHelper`.

### Q9: @Builder compile error with @NoArgsConstructor

**Cause**: Lombok version upgrade behavior change.

**Solution**: Add both `@NoArgsConstructor` and `@AllArgsConstructor`.

### Q10: AuthenticationManager cannot be injected

**Cause**: 2.7.x removed `authenticationManagerBean()` method.

**Solution**: Get from `AuthenticationConfiguration`:
```java
@Bean
public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
}
```

### Q11: Spring Security csrf().disable() deprecated warning

**Cause**: 2.7.x recommends Lambda DSL.

**Solution**: Use `http.csrf(csrf -> csrf.disable())`.

### Q12: JUnit 4 @RunWith annotation not working

**Cause**: 2.7.x uses JUnit 5 by default.

**Solution**: Remove `@RunWith(SpringRunner.class)`, JUnit 5 auto-integrates.

### Q13: @Test(expected = Exception.class) compile error

**Cause**: JUnit 5 removed `expected` attribute.

**Solution**: Use `assertThrows`:
```java
@Test
void testException() {
    assertThrows(UserNotFoundException.class, () -> {
        userService.findById(999L);
    });
}
```

### Q14: spring.factories auto-config not working

**Cause**: 2.7+ recommends new registration method.

**Solution**: Create `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` file.

### Q15: Actuator /info endpoint returns empty

**Cause**: 2.6+ `/info` endpoint doesn't expose env info by default.

**Solution**: Set `management.info.env.enabled=true`.

### Q16: JPA getOne() deprecated warning

**Cause**: `getOne()` deprecated in Spring Data JPA 2.5.

**Solution**: Use `getReferenceById()` instead.

### Q17: ShardingSphere dependency download failed

**Cause**: ShardingSphere 5.4.1 requires Alibaba Cloud private repository.

**Solution**: Configure Alibaba Cloud Maven repository:
```xml
<repository>
    <id>aliyun-releases</id>
    <url>https://packages.aliyun.com/maven/repository/2421751-release-ZmwRAc/</url>
</repository>
```

### Q18: Spring Boot Admin connection failed

**Cause**: Admin Server and Client version mismatch.

**Solution**: Upgrade both to 2.7.10.

### Q19: Redis configuration path changed

**Cause**: 2.7+ moved Redis config under `spring.data.redis`.

**Solution**: Both `spring.redis` and `spring.data.redis` work, new path recommended.

### Q20: Elasticsearch RestHighLevelClient deprecated warning

**Cause**: Elasticsearch 7.15+ deprecated RestHighLevelClient.

**Solution**: Migrate to new `ElasticsearchClient` or add `@SuppressWarnings("deprecation")`.

---

## 6. References

- [Spring Boot 2.5 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.5-Release-Notes)
- [Spring Boot 2.6 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.6-Release-Notes)
- [Spring Boot 2.7 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.7-Release-Notes)
- [Spring Security 5.7 Migration Guide](https://docs.spring.io/spring-security/reference/5.7/migration/index.html)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)