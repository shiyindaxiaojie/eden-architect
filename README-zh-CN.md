<img src="https://cdn.jsdelivr.net/gh/shiyindaxiaojie/eden-images/readme/icon.png" align="right" />

[license-apache2.0]:https://www.apache.org/licenses/LICENSE-2.0.html

[github-action]:https://github.com/shiyindaxiaojie/eden-architect/actions

[sonarcloud-dashboard]:https://sonarcloud.io/dashboard?id=shiyindaxiaojie_eden-architect

# Eden* Architect

![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/eden-images/readme/language-java-blue.svg) [![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/eden-images/readme/license-apache2.0-red.svg)][license-apache2.0] [![](https://github.com/shiyindaxiaojie/eden-architect/workflows/build/badge.svg)][github-action] [![](https://sonarcloud.io/api/project_badges/measure?project=shiyindaxiaojie_eden-architect&metric=alert_status)][sonarcloud-dashboard]

Eden* Architect 致力于提供企业开发的一站式解决方案。此项目包含开发分布式应用服务的必需组件，您只需要添加一些注解和少量配置，就可以将 Spring Boot 应用接入微服务解决方案，通过中间件来迅速搭建分布式应用系统。

> 参考文档请查看 [WIKI](https://github.com/shiyindaxiaojie/eden-architect/wiki) 。

## 主要功能

* **依赖管理和插件封装**：统一管理依赖版本，解决依赖冲突问题，并提供常用插件的封装，让开发者减少在构建工具所消耗的时间。
* **常用组件集成与封装**：在 Spring 官方的基础上扩展，提供 `XxlJob`、`CAT`、`Netty`、`Arthas` 等组件的集成。
* **组件适配及扩展点**：针对现有主流技术点进行高级抽象，提供 `消息队列`、`缓存`、`短信平台`、`邮件`、`Excel` 等组件的动态适配。
* **通用场景解决方案**：提供`多级缓存`、`分布式锁`、`分布式唯一ID`、`幂等性处理`、`业务流程编排`、`最终一致性`、`全链路标记` 等解决方案工具。

## 组件构成

![](https://cdn.jsdelivr.net/gh/shiyindaxiaojie/eden-images/eden-architect/component.png)

* **eden-dependencies**: 依赖管理组件，管理全局依赖的版本
* **eden-parent**: 构建管理组件，封装常用插件，提供开箱即用的配置
* **eden-commons**: 基础工具组件，基于 `Apache Commons`、`Google Guava` 、`HuTool` 扩展
* **eden-extensions**: 扩展点组件，参考 `Dubbo` 扩展点改造，轻量级实现组件的扩展
* **eden-cola**: `COLA` 组件，在 `COLA` 原生的基础上优化，完善了 `DDD` 领域模型、轻量级状态机、业务扩展点等组件
* **eden-solutions**: 解决方案工具集，提供 `多级缓存`、`分布式锁`、`分布式唯一ID`、`数据去重`、`事件审计` 等场景的设计与实现
* **eden-spring-framework**: 基础框架组件，支持自定义错误码、异常解析器
* **eden-spring-data**: 数据存储组件，扩展了 `Mybatis`、`Redis`、`Flyway`、`Liquibase` 等组件
* **eden-spring-security**: 授权认证组件，扩展了 `Spring Security OAuth2`、`Jwt`、`Shiro` 等组件
* **eden-spring-integration**: 第三方集成组件，扩展了 `RocketMQ`、`Kafka`、`Netty`、`XxlJob` 等组件
* **eden-spring-boot**: `Spring Boot`组件，根据实际的使用场景进行扩展
* **eden-spring-boot-starters**: `Spring Boot`组件自动装配，对官方原生组件无感知增强，并扩充未集成的组件
* **eden-spring-boot-test**: `Spring Boot`组件测试，对官方原生组件进行扩展
* **eden-spring-cloud**: `Spring Cloud`组件，扩展了 `Nacos`、`Sentinel`、`Zookeeper` 等组件
* **eden-spring-cloud-starters**: `Spring Cloud`组件自动装配，基于 `Spring Cloud Starters` 扩展
* **eden-spring-test**: `Spring`测试组件，扩展了 `TestContainer`测试容器和嵌入式的中间件，单元测试

## 如何构建

由于 `Spring Boot 2.4.x` 和 `Spring Boot 3.0.x` 在架构层面有很大的变更，因此我们采取跟 Spring Boot 版本号一致的分支:

* 2.4.x 分支适用于 `Spring Boot 2.4.x`，最低支持 JDK 1.8。
* 2.7.x 分支适用于 `Spring Boot 2.7.x`，最低支持 JDK 11。
* 3.0.x 分支适用于 `Spring Boot 3.0.x`，最低支持 JDK 17。

本项目默认使用 Maven 来构建，最快的使用方式是 `git clone` 到本地，然后执行以下命令：

```bash
./mvnw install
```

执行完毕后，项目将被安装到本地 Maven 仓库。

## 如何使用

### 如何集成到您的服务

首先，在您的项目 `pom.xml` 的 `parent` 节点引用 `eden-parent` 父工程。

```xml
<parent>
	<groupId>org.ylzl</groupId>
	<artifactId>eden-parent</artifactId>
	<version>1.0.0</version>
	<relativePath/>
</parent>
```

然后在 `dependencies` 节点中添加自己所需使用的依赖即可使用，例如，引入 Mybatis 组件。为了减少繁琐的依赖冲突解决工作，`eden-parent` 内置的 `eden-dependencies` 组件导入了常用的依赖，默认情况下，大多数组件依赖不需要填写版本号。

```xml
<dependency>
	<groupId>org.ylzl</groupId>
	<artifactId>eden-mybatis-spring-boot-starter</artifactId>
</dependency>
```

## 代码演示

为了演示如何使用，我们提供了 3 种不同架构风格的 Demo

* 面向领域模型的 **COLA 架构**，代码实例可以查看 [eden-demo-cola](https://github.com/shiyindaxiaojie/eden-demo-cola)
* 面向数据模型的 **分层架构**，代码实例请查看 [eden-demo-layer](https://github.com/shiyindaxiaojie/eden-demo-layer)
* 面向单机模型的 **MVC 架构**，代码实例可以查看 [eden-demo-mvc](https://github.com/shiyindaxiaojie/eden-demo-mvc)

## 版本规范

项目的版本号格式为 `x.y.z` 的形式，其中 x 的数值类型为数字，从 0 开始取值，且不限于 0~9 这个范围。项目处于孵化器阶段时，第一位版本号固定使用 0，即版本号为 `0.x.x` 的格式。

* 孵化版本：0.0.1-SNAPSHOT
* 开发版本：1.0.0-SNAPSHOT
* 发布版本：1.0.0

版本迭代规则：

* 1.0.0 <> 1.0.1：兼容
* 1.0.0 <> 1.1.0：基本兼容
* 1.0.0 <> 2.0.0：不兼容

## 变更日志

请查阅 [CHANGELOG.md](https://github.com/shiyindaxiaojie/eden-architect/blob/main/CHANGELOG.md)
