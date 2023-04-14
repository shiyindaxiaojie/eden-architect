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

package org.ylzl.eden.cat.spring.boot.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Cat 属性配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "cat")
public class CatProperties {

	public static final String PREFIX = "cat";

	private boolean enabled;

	private boolean traceMode = false;

	private boolean supportOutTraceId = false;

	private String home = "/tmp";

	private String domain;

	private String servers;

	private int tcpPort = 2280;

	private int httpPort = 80;

	private final Http http = new Http();

	private final Dubbo dubbo = new Dubbo();

	private final Mybatis mybatis = new Mybatis();

	private final Redis redis = new Redis();

	private final RocketMQ rocketMQ = new RocketMQ();

	private final Kafka kafka = new Kafka();

	@Getter
	@Setter
	public static class Http {

		private boolean enabled = true;

		private String excludeUrls = "/favicon.ico,/js/*,/css/*,/image/*";

		private String includeHeaders;

		private boolean includeBody = false;
	}

	@Getter
	@Setter
	public static class Dubbo {

		private boolean enabled = true;
	}

	@Getter
	@Setter
	public static class Mybatis {

		private boolean enabled = true;
	}

	@Getter
	@Setter
	public static class Redis {

		private boolean enabled = true;
	}

	@Getter
	@Setter
	public static class RocketMQ {

		private boolean enabled = true;
	}

	@Getter
	@Setter
	public static class Kafka {

		private boolean enabled = true;
	}
}
