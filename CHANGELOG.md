# Changelog

## 0.0.2-SNAPSHOT(Fri ?, 2023)

> Coming soon...

### FEATURES

- 自研流程编排组件，支持配置中心动态刷新流程，详见 [`eden-flow-compose`](https://github.com/shiyindaxiaojie/eden-architect/tree/main/eden-components/eden-solutions/eden-flow-compose)
- 自研一致性框架，支持 MQ 和 Cache
  实现最终一致性，详见 [`eden-consistency-task`](https://github.com/shiyindaxiaojie/eden-architect/tree/main/eden-components/eden-solutions/eden-consistency-task)
- 自研全链路标记组件，支持常用组件的影子库切换，详见 [`eden-full-tracing`](https://github.com/shiyindaxiaojie/eden-architect/tree/main/eden-components/eden-solutions/eden-full-tracing)
- 封装授权认证组件，支持 `OAuth2`、`JWT`
  无缝切换，详见 [`eden-common-security`](https://github.com/shiyindaxiaojie/eden-architect/tree/main/eden-components/eden-solutions/eden-dynamic-security)
- 封装数据审计组件，提供数据比对、数据脱敏功能，详见 [`eden-data-auditor`](https://github.com/shiyindaxiaojie/eden-architect/tree/main/eden-components/eden-solutions/eden-data-auditor)
- 封装数据过滤组件，提供数据去重、敏感词过滤功能，详见 [`eden-data-filter`](https://github.com/shiyindaxiaojie/eden-architect/tree/main/eden-components/eden-solutions/eden-data-filter)
- 封装事件审计组件，支持 SpEL
  表达式和自定义函数，详见 [`eden-event-auditor`](https://github.com/shiyindaxiaojie/eden-architect/tree/main/eden-components/eden-solutions/eden-event-auditor)
- 封装 `Excel` 组件，目前阿里的 `EasyExcel`
  做的比较好，暂时只接入这块，详见 [`eden-dynamic-excel`](https://github.com/shiyindaxiaojie/eden-architect/tree/main/eden-components/eden-solutions/eden-dynamic-excel)

### IMPROVEMENTS

- `Nacos` 支持配置 log4j2.yaml，并实现动态刷新，方便您动态扩展 Appender 输出日志位置和日志级别。
- `JSON` 序列化通用组件支持，支持 SPI 扩展，目前内置集成了 `Jackson`、`Fastjson`、`Fastjson2`
- `SpEL` 表达式封装，提供简易的调用方法和上下文支持

## 0.0.1-SNAPSHOT(Dec 31, 2022)

### FEATURES

- Maven
  依赖管理，封装插件，提供开箱即用的配置，详见 [`eden-dependencies`](https://github.com/shiyindaxiaojie/eden-architect/tree/main/eden-components/eden-dependencies)
  和 [`eden-parent`](https://github.com/shiyindaxiaojie/eden-architect/tree/main/eden-components/eden-parent)
- Spring 官方组件改进，补充官方未提供的 `Spring Boot Starter`
  组件，详见 [`eden-spring-boot-starters`](https://github.com/shiyindaxiaojie/eden-architect/tree/main/eden-components/eden-spring-boot-starters)
  和
  [`eden-spring-cloud-starters`](https://github.com/shiyindaxiaojie/eden-architect/tree/main/eden-components/eden-spring-cloud-starters)
- 基于 `Dubbo`
  扩展，提供轻量级的扩展点组件，详见 [`eden-extensions`](https://github.com/shiyindaxiaojie/eden-architect/tree/main/eden-components/eden-extensions)
- 基于 `COLA4` 扩展，重构 `DTO`
  、状态机、业务扩展点等组件，详见 [`eden-cola`](https://github.com/shiyindaxiaojie/eden-architect/tree/main/eden-components/eden-cola)
- 封装分布式缓存组件，支持 `Redis`、`Caffeine`、`Guava` 无缝切换，接入 `JD HotKey` 或者 `Sentinel`
  可实现多级缓存动态切换，详见 [`eden-common-cache`](https://github.com/shiyindaxiaojie/eden-architect/tree/main/eden-components/eden-solutions/eden-common-cache)
- 封装消息队列组件，支持 `RocketMQ`、`Kafka`、`Pulsar`
  无缝切换，详见 [`eden-common-mq`](https://github.com/shiyindaxiaojie/eden-architect/tree/main/eden-components/eden-solutions/eden-common-mq)
- 封装分布式锁组件，支持 `Redisson`、`Jedis`、`Curator`、`Zookeeper`
  无缝切换，详见 [`eden-distrubuted-lock`](https://github.com/shiyindaxiaojie/eden-architect/tree/main/eden-components/eden-solutions/eden-distrubuted-lock)
- 封装分布式唯一ID组件，支持 `Leaf`、`UIDGenerator`、`TinyId`、`Snowflake`
  无缝切换，详见 [`eden-distrubuted-uid`](https://github.com/shiyindaxiaojie/eden-architect/tree/main/eden-components/eden-solutions/eden-distrubuted-uid)
- 封装幂等性处理组件，提供 `Token` （校验令牌）或者 `TTL`
  （过期时间）两种解决方案，详见 [`eden-idempotent`](https://github.com/shiyindaxiaojie/eden-architect/tree/main/eden-components/eden-solutions/eden-idempotent)
- 封装 SMS
  组件，支持阿里云、腾讯云、梦网、亿美等短信平台组件无缝切换，详见 [`eden-common-sms`](https://github.com/shiyindaxiaojie/eden-architect/tree/main/eden-components/eden-solutions/eden-common-sms)
- 封装 Mail 组件，由于使用较少，目前仅接入 `JavaMail`
  组件，详见 [`eden-common-mail`](https://github.com/shiyindaxiaojie/eden-architect/tree/main/eden-components/eden-solutions/eden-common-mail)

### IMPROVEMENTS

- `ErrorCode` 错误码通用设计，支持业务自定义错误码和国际化语言，基于 `eden-extensions` 的 SPI 扩展自定义返回对象，此场景适用于前后端联调
- `MyBatis` 增强，支持日志打印原生 SQL 解析语句和执行耗时
- `Sentinel` 增强，支持 `Web`、`Dubbo` 等场景的流控，并提供 `Spring Boot Actuator` 监控端点
- `XxlJob` 增强，支持应用动态注册到 XxlJob控制台
- `Sleuth` 增强，增加 `Zipkin` 链路跟踪 HTTP 报文信息

[//]: # (### BUG FIXES)

[//]: # (### BREAKING CHANGES:)
