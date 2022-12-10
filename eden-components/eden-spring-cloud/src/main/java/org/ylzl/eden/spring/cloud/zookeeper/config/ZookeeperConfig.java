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
