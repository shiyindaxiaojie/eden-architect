# Eden* Architect 开发规范指南

> 本文档基于项目现有代码风格制定，旨在保持代码一致性和可维护性。

## 📋 目录

- [1. 概述](#1-概述)
- [2. 命名规范](#2-命名规范)
- [3. 代码格式规范](#3-代码格式规范)
- [4. 注释规范](#4-注释规范)
- [5. 包结构规范](#5-包结构规范)
- [6. 模块设计规范](#6-模块设计规范)
- [7. 接口设计规范](#7-接口设计规范)
- [8. 设计模式规范](#8-设计模式规范)
- [9. 异常处理规范](#9-异常处理规范)
- [10. 日志规范](#10-日志规范)
- [11. 测试规范](#11-测试规范)
- [12. Maven 规范](#12-maven-规范)
- [13. Git 工作流规范](#13-git-工作流规范)
- [14. 数据库规范](#14-数据库规范)

---

## 1. 概述

Eden* Architect 是一个基于 Spring Boot 2.7.x 的自研应用架构，采用 COLA（Clean Object-Oriented and Layered Architecture）架构设计。本规范旨在统一项目的编码风格，提高代码的可读性和可维护性。

### 1.1 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 1.8+ | 基础运行环境 |
| Spring Boot | 2.7.18 | 核心框架 |
| Lombok | - | 代码简化 |
| MapStruct | - | 对象映射 |
| Spock | - | 测试框架 |

---

## 2. 命名规范

### 2.1 类命名

遵循驼峰命名法（CamelCase），使用有意义的英文单词或缩写。

| 类型 | 命名规则 | 示例 |
|------|----------|------|
| 工具类 | `*Utils` | `BeanUtils`, `CollectionUtils`, `StringUtils` |
| 异常类 | `*Exception` | `BeanConvertException`, `DecryptException` |
| 自动配置类 | `*AutoConfiguration` | `RedisCacheAutoConfiguration` |
| 配置属性类 | `*Properties` | `RedisProperties` |
| 环境类 | `*Environment` | `RedisEnvironment` |
| 接口 | 不使用 `I` 前缀 | `Search`, `Sort` |
| 抽象类 | `Abstract*` | `AbstractSort` |

### 2.2 方法命名

```java
// 获取方法
public User getUser(Long id);
public List<User> listUsers();
public Optional<User> findById(Long id);

// 布尔判断方法
public boolean isEmpty();
public boolean hasNext();
public boolean canExecute();

// 转换方法
public UserDTO toDTO(User user);
public User toBean(Map<?, ?> map, Class<User> clazz);

// 工厂方法
public static Builder builder();
public static User create();
```

### 2.3 变量命名

```java
// 常量：全大写，下划线分隔
private static final String BEAN_REDIS_CACHE_MGR = "redisCacheManager";
private static final String CAN_NOT_CAST_TO_TYPE = "Field '{}' value '{}' can not cast to type {}";

// 成员变量
private final RedisConnectionFactory redisConnectionFactory;

// 日志消息常量
public static final String AUTOWIRED_REDIS_CACHE_MANAGER = "Autowired RedisCacheManager";
```

### 2.4 包命名

```
org.ylzl.eden                          # 根包
├── commons                            # 公共工具组件
│   ├── algorithms                     # 算法相关
│   ├── bean                           # Bean 工具
│   │   └── exception                  # Bean 异常
│   ├── codec                          # 编解码
│   ├── collections                    # 集合工具
│   └── lang                           # 语言扩展
├── extensions                         # 扩展点组件
├── {技术}.spring.boot                 # Spring Boot 集成
│   ├── autoconfigure                  # 自动配置
│   └── env                            # 环境配置
└── cola                               # COLA 架构组件
    ├── domain                         # 领域层
    ├── dto                            # 数据传输对象
    └── extension                      # 扩展点
```

---

## 3. 代码格式规范

### 3.1 缩进与空格

基于 `.editorconfig` 配置：

```ini
# Java/Groovy/XML 文件
indent_style = tab
indent_size = 4

# 其他文件
indent_style = space
indent_size = 2
```

### 3.2 代码格式示例

```java
@ConditionalOnBean(RedisConnectionFactory.class)
@ConditionalOnClass({RedisOperations.class, RedisCacheManager.class})
@EnableCaching
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class RedisCacheAutoConfiguration extends CachingConfigurerSupport {

	private static final String BEAN_REDIS_CACHE_MGR = "redisCacheManager";

	private final RedisConnectionFactory redisConnectionFactory;

	public RedisCacheAutoConfiguration(RedisConnectionFactory redisConnectionFactory) {
		this.redisConnectionFactory = redisConnectionFactory;
	}

	@ConditionalOnMissingBean(name = BEAN_REDIS_CACHE_MGR)
	@Qualifier(BEAN_REDIS_CACHE_MGR)
	@Bean
	@Override
	public CacheManager cacheManager() {
		log.debug(AUTOWIRED_REDIS_CACHE_MANAGER);
		return RedisCacheManager.create(redisConnectionFactory);
	}
}
```

### 3.3 注解顺序

按以下顺序排列注解：

1. 条件注解 (`@Conditional*`)
2. 功能注解 (`@EnableCaching`, `@EnableAsync`)
3. Lombok 注解 (`@Slf4j`, `@Data`)
4. Spring 元数据注解 (`@Role`)
5. 配置注解 (`@Configuration`, `@Component`)

---

## 4. 注释规范

### 4.1 文件头（License）

所有 Java 文件必须包含 Apache 2.0 协议头：

```java
/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
```

### 4.2 类注释

```java
/**
 * 对象实体工具集
 *
 * <p>变更日志：Spring Boot 升级 1.X 到 2.X
 *
 * <ul>
 *   <li>{@link RedisCacheManager} 移除构造器参数 {@link org.springframework.data.redis.core.RedisTemplate}
 *   <li>{@link RedisCacheManager} 使用 {@code create()} 创建实例
 * </ul>
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @see org.apache.commons.beanutils.BeanUtils
 * @since 2.4.13
 */
```

### 4.3 方法注释

```java
/**
 * 将 Map 转换为指定类型的 Bean
 *
 * @param sourceMap 源数据 Map
 * @param targetClass 目标类型
 * @param <T> 目标类型泛型
 * @return 转换后的 Bean 对象
 * @throws BeanConvertException 转换失败时抛出
 */
public static <T> T toBean(@NonNull Map<?, ?> sourceMap, @NonNull Class<T> targetClass) 
    throws BeanConvertException {
    // ...
}
```

### 4.4 包注释

每个包应包含 `package-info.java` 文件：

```java
/*
 * Copyright 2012-2019 the original author or authors.
 * ...
 */

package org.ylzl.eden.commons.algorithms;
```

---

## 5. 包结构规范

### 5.1 模块层级

```
eden-architect/
├── eden-agents/                    # AI 代理组件
├── eden-components/                # 核心组件
│   ├── eden-cola/                  # COLA 架构组件
│   ├── eden-commons/               # 公共工具
│   ├── eden-dependencies/          # 依赖管理
│   ├── eden-extensions/            # 扩展点
│   ├── eden-parent/                # 父 POM
│   ├── eden-solutions/             # 解决方案
│   ├── eden-spring-boot/           # Spring Boot 扩展
│   ├── eden-spring-boot-starters/  # Spring Boot Starter 集合
│   ├── eden-spring-cloud/          # Spring Cloud 扩展
│   ├── eden-spring-data/           # Spring Data 扩展
│   ├── eden-spring-framework/      # Spring Framework 扩展
│   ├── eden-spring-integration/    # Spring Integration 扩展
│   ├── eden-spring-security/       # Spring Security 扩展
│   └── eden-spring-test/           # 测试支持
├── eden-plugins/                   # 插件
└── eden-tests/                     # 测试模块
```

### 5.2 Starter 模块命名

```
eden-{技术名称}-spring-boot-starter
```

示例：
- `eden-redis-spring-boot-starter`
- `eden-mybatis-spring-boot-starter`
- `eden-kafka-spring-boot-starter`

---

## 6. 模块设计规范

### 6.1 AutoConfiguration 类

```java
@ConditionalOnBean(RequiredBean.class)           // 条件：依赖的 Bean
@ConditionalOnClass({RequiredClass.class})       // 条件：类路径
@ConditionalOnProperty(prefix = "xxx", name = "enabled", havingValue = "true")
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)        // 基础设施角色
@Configuration(proxyBeanMethods = false)         // 禁用代理（推荐）
public class XxxAutoConfiguration {

    public static final String AUTOWIRED_XXX = "Autowired Xxx";

    private static final String BEAN_NAME = "xxxBean";

    @ConditionalOnMissingBean(name = BEAN_NAME)
    @Bean
    public XxxBean xxxBean() {
        log.debug(AUTOWIRED_XXX);
        return new XxxBean();
    }
}
```

### 6.2 Properties 类

```java
@Data
@ConfigurationProperties(prefix = "eden.xxx")
public class XxxProperties {

    private boolean enabled = true;
    
    private String name = "default";
    
    private Duration timeout = Duration.ofSeconds(30);
}
```

### 6.3 spring.factories 配置

```properties
# Auto Configure
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.ylzl.eden.xxx.spring.boot.autoconfigure.XxxAutoConfiguration,\
org.ylzl.eden.xxx.spring.boot.autoconfigure.XxxCacheAutoConfiguration
```

---

## 7. 接口设计规范

### 7.1 接口定义

接口应简洁、职责单一，使用中文 Javadoc 描述：

```java
/**
 * 缓存接口
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface Cache {

	/**
	 * 获取缓存类型
	 *
	 * @return 缓存类型
	 */
	String getCacheType();

	/**
	 * 获取指定key的缓存项
	 *
	 * @param key 缓存Key
	 * @return 缓存项
	 */
	Object get(Object key);
}
```

### 7.2 Default 方法

Java 8+ 接口可使用 `default` 方法提供默认实现：

```java
default Object putIfAbsent(Object key, Object value) {
	Object existingValue = get(key);
	if (existingValue == null) {
		put(key, value);
	}
	return existingValue;
}
```

### 7.3 流式 API（Fluent API）

Builder 类应支持链式调用：

```java
public interface CacheBuilder {
	CacheBuilder cacheName(String cacheName);
	CacheBuilder cacheConfig(CacheConfig cacheConfig);
	Cache build();
}
```

---

## 8. 设计模式规范

### 8.1 Builder 模式

```java
public abstract class AbstractCacheBuilder implements CacheBuilder {

	private String cacheName;

	@Override
	public CacheBuilder cacheName(String cacheName) {
		this.cacheName = cacheName;
		return this;
	}
}
```

### 8.2 模板方法模式

```java
public abstract class AbstractSort<T> implements Sort<T> {

	@Override
	public T[] sort(T[] unsorted) {
		return doSort(unsorted);  // 模板方法
	}

	protected abstract T[] doSort(T[] unsorted);  // 抽象方法
}
```

### 8.3 SPI 扩展点机制

使用 `ExtensionLoader` 实现服务发现：

```java
// 获取默认扩展
L1CacheRemovalListener listener = ExtensionLoader
    .getExtensionLoader(L1CacheRemovalListener.class)
    .getDefaultExtension();
```

### 8.4 配置类区分

| 类型 | 命名 | 用途 |
|------|------|------|
| `*Config` | `CacheConfig` | 运行时配置（非 Spring 管理） |
| `*Properties` | `CacheProperties` | Spring Boot 属性类 |

---

## 9. 异常处理规范

### 9.1 自定义异常类

```java
/**
 * Bean 转换异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class BeanConvertException extends RuntimeException {

	public BeanConvertException(String message) {
		super(message);
	}

	public BeanConvertException(String message, Throwable ex) {
		super(message, ex);
	}
}
```

### 7.2 异常处理原则

1. **使用 RuntimeException 子类**：避免 Checked Exception 的传播污染
2. **保留异常链**：使用 `new Exception(message, cause)` 构造器
3. **有意义的消息**：使用格式化消息模板

```java
throw new BeanConvertException(
    MessageFormatUtils.format(CAN_NOT_CAST_TO_TYPE, field.getName(), value, field.getType()), 
    ex
);
```

---

## 10. 日志规范

### 10.1 日志框架

使用 Lombok 的 `@Slf4j` 注解：

```java
@Slf4j
public class MyService {
    
    public void doSomething() {
        log.debug("Processing started");
        log.info("User {} logged in", username);
        log.warn("Connection timeout, retrying...");
        log.error("Failed to process: {}", e.getMessage(), e);
    }
}
```

### 10.2 日志级别使用

| 级别 | 使用场景 |
|------|----------|
| `trace` | 详细的调试信息，通常仅开发环境使用 |
| `debug` | 调试信息，如 Bean 自动装配、方法调用 |
| `info` | 重要的业务流程信息 |
| `warn` | 警告信息，潜在问题但不影响运行 |
| `error` | 错误信息，需要关注和处理 |

### 10.3 日志消息常量

将日志消息定义为常量：

```java
public static final String AUTOWIRED_REDIS_CACHE_MANAGER = "Autowired RedisCacheManager";

// 使用
log.debug(AUTOWIRED_REDIS_CACHE_MANAGER);
```

---

## 11. 测试规范

### 11.1 测试框架

| 框架 | 用途 |
|------|------|
| Spock | BDD 风格单元测试（推荐） |
| JUnit 5 | 传统单元测试 |
| Mockito | Mock 框架 |
| JMH | 性能基准测试 |

### 11.2 测试目录结构

```
src/
├── main/java/          # 主代码
└── test/
    ├── groovy/         # Spock 测试（推荐）
    └── java/           # JUnit 测试
```

### 11.3 Spock 测试示例

```groovy
class BeanUtilsSpec extends Specification {

    def "toBean should convert map to object"() {
        given: "a source map"
        def map = [name: "test", age: 18]
        
        when: "converting to bean"
        def result = BeanUtils.toBean(map, User.class)
        
        then: "bean should have correct values"
        result.name == "test"
        result.age == 18
    }
}
```

---

## 12. Maven 规范

### 12.1 POM 依赖组织

使用 XML 注释对依赖进行分组：

```xml
<dependencies>
    <!-- 内部组件 -->
    <dependency>
        <groupId>io.github.shiyindaxiaojie</groupId>
        <artifactId>eden-commons</artifactId>
    </dependency>

    <!-- 开发组件 -->
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- Redis -->
    <dependency>
        <groupId>org.springframework.data</groupId>
        <artifactId>spring-data-redis</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- 测试组件 -->
    <dependency>
        <groupId>org.spockframework</groupId>
        <artifactId>spock-core</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### 12.2 依赖分组顺序

1. 内部组件（`io.github.shiyindaxiaojie`）
2. 开发组件（`mapstruct`, `lombok`）
3. Spring 相关组件
4. 第三方组件（按功能分类）
5. 测试组件

### 12.3 可选依赖

非必需的依赖应标记为 `optional`：

```xml
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-redis</artifactId>
    <optional>true</optional>
</dependency>
```

---

## 13. Git 工作流规范

### 13.1 分支模型

| 分支 | 说明 |
|------|------|
| `master` | 主分支，稳定版本 |
| `develop` | 开发分支 |
| `feature/xxx` | 新功能分支 |
| `fix/xxx` | Bug 修复分支 |

### 13.2 提交消息格式

遵循 Conventional Commits 规范：

```
<type>(<scope>): <subject>

<body>

<footer>
```

#### Type 类型

| 类型 | 说明 |
|------|------|
| `feat` | 新功能 |
| `fix` | Bug 修复 |
| `docs` | 文档更新 |
| `style` | 代码格式（不影响功能） |
| `refactor` | 重构 |
| `perf` | 性能优化 |
| `test` | 测试相关 |
| `chore` | 构建/工具相关 |

#### 示例

```
feat(redis): add Redis cache auto configuration

fix(mybatis): fix SQL injection vulnerability

docs(readme): update installation guide
```

### 13.3 提交规则

- 使用现在时态（"Add feature" 而非 "Added feature"）
- 使用祈使语气（"Move cursor to..." 而非 "Moves cursor to..."）
- 首行限制在 72 个字符以内
- 首行后可引用 Issue 和 Pull Request

---

## 14. 数据库规范

### 14.1 外键约束

> ⚠️ **重要**：数据库不允许使用外键约束

外键关系应在应用层维护，避免数据库级别的外键约束。

### 14.2 索引规范

- 非必要不要创建过多索引
- 主键自动创建索引
- 只为高频查询字段创建索引
- 避免在频繁更新的字段上创建索引

### 14.3 命名规范

| 类型 | 命名规则 | 示例 |
|------|----------|------|
| 表名 | 小写下划线 | `user_info`, `order_detail` |
| 字段名 | 小写下划线 | `user_name`, `created_at` |
| 主键 | `id` 或 `{表名}_id` | `id`, `user_id` |
| 索引 | `idx_{表名}_{字段名}` | `idx_user_name` |
| 唯一索引 | `uk_{表名}_{字段名}` | `uk_user_email` |

---

## 附录

### A. 常用 Lombok 注解

| 注解 | 说明 |
|------|------|
| `@Slf4j` | 生成 SLF4J 日志对象 |
| `@Data` | 生成 getter/setter/toString/equals/hashCode |
| `@Builder` | 生成 Builder 模式代码 |
| `@NonNull` | 非空校验 |
| `@UtilityClass` | 工具类（私有构造器 + 静态方法校验） |
| `@RequiredArgsConstructor` | 生成 final 字段的构造器 |

### B. IDE 配置

推荐导入项目根目录的 `.editorconfig` 配置，确保代码格式一致。

### C. 相关文档

- [CONTRIBUTING.md](../../CONTRIBUTING.md) - 贡献指南
- [CODE_OF_CONDUCT.md](../../CODE_OF_CONDUCT.md) - 行为准则
- [CHANGELOG.md](../../CHANGELOG.md) - 变更日志
