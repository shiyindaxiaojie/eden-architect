package org.ylzl.eden.sample.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * 消息队列自动配置
 *
 * @author gyl
 * @since 1.0.0
 */
@EnableKafka
@Slf4j
@Configuration
public class MessagingAutoConfiguration {}
