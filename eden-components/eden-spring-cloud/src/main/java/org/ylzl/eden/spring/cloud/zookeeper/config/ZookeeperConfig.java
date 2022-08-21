package org.ylzl.eden.spring.cloud.zookeeper.config;

import lombok.*;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Zookeeper 配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Data
public class ZookeeperConfig {

	private String connectString = "localhost:2181";

	private Integer baseSleepTimeMs = 50;

	private Integer maxRetries = 10;

	private Integer maxSleepMs = 500;

	private Integer blockUntilConnectedWait = 10;

	private TimeUnit blockUntilConnectedUnit;

	private Duration sessionTimeout;

	private Duration connectionTimeout;
}
