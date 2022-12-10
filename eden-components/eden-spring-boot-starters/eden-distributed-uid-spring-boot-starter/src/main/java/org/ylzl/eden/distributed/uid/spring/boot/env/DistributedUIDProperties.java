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

package org.ylzl.eden.distributed.uid.spring.boot.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ylzl.eden.distributed.uid.spring.boot.support.DistributedUIDBeanNames;

/**
 * 分布式唯一ID配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Setter
@Getter
@ConfigurationProperties(prefix = DistributedUIDProperties.PREFIX)
public class DistributedUIDProperties {

	public static final String PREFIX = "distributed-uid";

	private DistributedUIDBeanNames primary;

	private Boolean enabled;

	private final Leaf leaf = new Leaf();

	private final Snowflake snowflake = new Snowflake();

	private final TinyId tinyId = new TinyId();

	private final UidGenerator uidGenerator = new UidGenerator();

	@Setter
	@Getter
	public static class Leaf {

		public static final String PREFIX = DistributedUIDProperties.PREFIX + ".leaf";

		private Boolean enabled;
	}

	@Setter
	@Getter
	public static class Snowflake {

		public static final String PREFIX = DistributedUIDProperties.PREFIX + ".snowflake";

		private Boolean enabled;
	}

	@Setter
	@Getter
	public static class TinyId {

		public static final String PREFIX = DistributedUIDProperties.PREFIX + ".tiny-id";

		private Boolean enabled;
	}

	@Setter
	@Getter
	public static class UidGenerator {

		public static final String PREFIX = DistributedUIDProperties.PREFIX + ".uid-generator";

		private Boolean enabled;
	}
}
