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
import org.ylzl.eden.distributed.uid.config.SegmentGeneratorConfig;
import org.ylzl.eden.distributed.uid.config.SnowflakeGeneratorConfig;

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

	public static final String ID_GENERATOR_PREFIX = PREFIX + ".id-generator";

	public static final String SEGMENT_GENERATOR_PREFIX = PREFIX + ".segment-generator";

	private final SnowflakeGeneratorConfig snowflakeGenerator = new SnowflakeGeneratorConfig();

	private final SegmentGeneratorConfig segmentGenerator = new SegmentGeneratorConfig();
}
