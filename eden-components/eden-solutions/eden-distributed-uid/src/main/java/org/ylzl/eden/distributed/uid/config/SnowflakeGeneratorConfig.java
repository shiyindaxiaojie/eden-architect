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

package org.ylzl.eden.distributed.uid.config;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ID 生成器配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@EqualsAndHashCode
@ToString
@Setter
@Getter
public class SnowflakeGeneratorConfig {

	private boolean enabled = false;

	private String type = "leaf";

	private String name = System.getProperty("spring.application.name", "app");

	private long twepoch = 1288834974657L;

	private final Coordinator coordinator = new Coordinator();

	@EqualsAndHashCode
	@ToString
	@Setter
	@Getter
	public static class Coordinator {

		private String type = "zookeeper";

		private final Zookeeper zookeeper = new Zookeeper();

		@EqualsAndHashCode
		@ToString
		@Setter
		@Getter
		public static class Zookeeper {

			private String connectString = "localhost:2181";
		}
	}
}
