<img src="https://cdn.jsdelivr.net/gh/eden-lab/eden-lab-images/readme/icon.png" align="right" />

[license-apache2.0]:https://www.apache.org/licenses/LICENSE-2.0.html
[github-action]:https://github.com/eden-lab/eden-architect/actions
[sonarcloud-dashboard]:https://sonarcloud.io/dashboard?id=eden-lab_eden-architect

# Eden* Architect 

![](https://cdn.jsdelivr.net/gh/eden-lab/eden-lab-images/readme/language-java-blue.svg) [![](https://cdn.jsdelivr.net/gh/eden-lab/eden-lab-images/readme/license-apache2.0-red.svg)][license-apache2.0] [![](https://github.com/eden-lab/eden-architect/workflows/build/badge.svg)][github-action] [![](https://sonarcloud.io/api/project_badges/measure?project=eden-lab_eden-architect&metric=alert_status)][sonarcloud-dashboard]

Eden* Architect 致力于提供企业开发的一站式解决方案。此项目包含开发分布式应用服务的必需组件，您只需要添加一些注解和少量配置，就可以将 Spring Boot 应用接入微服务解决方案，通过中间件来迅速搭建分布式应用系统。

> 参考文档请查看 [WIKI](https://github.com/eden-lab/eden-architect/wiki) 。

## 主要功能

* **Maven 依赖和插件封装**：解决了主流技术组件集成产生的依赖冲突问题，并提供相关组件的 `Spring Boot Starter`，为开发者减少在组件集成消耗的时间。另外，提供了各种 Maven 插件（几乎覆盖市面上 99% 的组件），降低了各种 Maven 插件集成的复杂度，开发者只需要直接引入插件就可以实现灵活的构建操作。
* **数据访问能力封装**：提供了 `Spring Data Redis`、`Spring Data Elasticsearch`、`Spring Data MongoDB` 等集成能力，并解决 `Mybatis Plus` 和 `PageHelper` 的冲突问题，开发者可以同时使用两者的特性，并提供了多数据源访问的支持，开发者通过 `@DS` 注解就可以直接完成数据源的切换。
* **数据分片能力封装**：提供了 `Sharding JDBC` 的封装，兼容 `Mybatis Plus`、`DynamicDataSource` 组件。
* **常用组件集成**：提供了 `Kafka`、`XXLJob`、`CAT`、`Zookeeper`、`EasyExcel`、`Sentinel` 等组件的集成，并提供 `Spring Boot Starter` 能力。

## 组件构成

![](https://cdn.jsdelivr.net/gh/eden-lab/eden-lab-images/eden-architect/component.png)

* **eden-components**: 组件集
* **eden-dependencies**: 依赖管理组件
* **eden-parent**: 构建管理组件
* **eden-commons**: 基础工具组件，集成了 `Apache Commons`、`Google Guava` 、`HuTool`...
* **eden-spring-boot-starters**: 常用组件自动装配
* **eden-spring-framework**: 基础框架组件
* **eden-spring-cloud**: 微服务框架组件，集成了 `Nacos`、`Sentinel`、`Apollo`...
* **eden-spring-cloud-starters**: 微服务框架组件自动装配
* **eden-spring-data**: 数据仓库组件，集成了 `Mybatis`、`Redis`、`Flyway`、`Liquibase`...
* **eden-spring-integration**: 第三方集成组件，集成了 `RocketMQ`、`Kafka`、`Netty`、`Swagger`...
* **eden-spring-security**: 授权认证组件，集成了 `Spring Security OAuth2`、`Jwt`...
* **eden-spring-test**: 单元测试、集成测试组件

## 如何构建
* master 分支对应的是 `Spring Boot 2.4.x`，最低支持 JDK 1.8。
* 1.5.x 分支对应的是 `Spring Boot 1.5.x`，最低支持 JDK 1.8。
* 2.4.x 分支对应的是 `Spring Boot 2.4.x`，最低支持 JDK 1.8。

本项目使用 Maven 来构建，最快的使用方式是 clone 到本地，然后执行以下命令：

```bash
./mvnw install
```

执行完毕后，项目将被安装到本地 Maven 仓库。

## 如何使用

### 如何集成到您的服务

如果需要使用已发布的版本，在 `parent` 中添加如下配置。

```xml
<parent>
    <groupId>org.ylzl</groupId>
    <artifactId>eden-parent</artifactId>
    <version>2.4.13.RELEASE</version>
    <relativePath/>
</parent>
```

然后在 `dependencies` 中添加自己所需使用的依赖即可使用，例如，引入 Mybatis 组件。

```xml
<dependency>
    <groupId>org.ylzl</groupId>
    <artifactId>eden-mybatis-spring-boot-starter</artifactId>
</dependency>
```

> 请注意，我们已经把常用的依赖纳入 eden-dependencies 管理，不建议带版本号覆盖原有的依赖。

## 代码演示

为了演示如何使用，我们提供了两种不同架构风格的 Demo
* 面向领域模型的 COLA 架构，代码实例可以查看 [eden-demo-cola](https://github.com/eden-lab/eden-demo-cola)
* 面向单机模型的 MVC 架构，代码实例可以查看 [eden-demo-mvc](https://github.com/eden-lab/eden-demo-mvc)
* 面向数据模型的分层架构，代码实例请查看 [eden-demo-layer](https://github.com/eden-lab/eden-demo-layer)

## 版本规范

项目的版本号格式为 x.x.x 的形式，其中 x 的数值类型为数字，从 0 开始取值，且不限于 0~9 这个范围。项目处于孵化器阶段时，第一位版本号固定使用 0，即版本号为 0.x.x 的格式。

由于 `Spring Boot 1.5.x` 和 `Spring Boot 2.4.x` 在架构层面有很大的变更，因此我们采取跟 Spring Boot 版本号一致的版本:

* 1.5.x 版本适用于 `Spring Boot 1.5.x`
* 2.4.x 版本适用于 `Spring Boot 2.4.x`
