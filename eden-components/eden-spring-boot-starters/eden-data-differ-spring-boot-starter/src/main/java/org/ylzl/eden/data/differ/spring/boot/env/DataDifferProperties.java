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

package org.ylzl.eden.data.differ.spring.boot.env;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.javers.core.diff.ListCompareAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ylzl.eden.data.differ.config.DataDifferConfig;

/**
 * 数据比对配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Setter
@Getter
@ConfigurationProperties(prefix = DataDifferProperties.PREFIX)
public class DataDifferProperties {

	public static final String PREFIX = "data-differ";

	private String primary = "javers";

	private final DataDifferConfig.Javers javers = new DataDifferConfig.Javers();

	private final DataDifferConfig.JavaObjectDiff javaObjectDiff = new DataDifferConfig.JavaObjectDiff();

	@EqualsAndHashCode
	@ToString
	@Setter
	@Getter
	public static class Javers {

		private ListCompareAlgorithm listCompareAlgorithm = ListCompareAlgorithm.LEVENSHTEIN_DISTANCE;

		private boolean usePrimitiveDefaults = false;
	}

	@EqualsAndHashCode
	@ToString
	@Setter
	@Getter
	public static class JavaObjectDiff {

	}
}
