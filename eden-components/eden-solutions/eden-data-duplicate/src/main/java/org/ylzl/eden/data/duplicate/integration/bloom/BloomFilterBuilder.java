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

package org.ylzl.eden.data.duplicate.integration.bloom;

import org.ylzl.eden.data.duplicate.DuplicateFilter;
import org.ylzl.eden.data.duplicate.builder.AbstractDuplicateFilterBuilder;
import org.ylzl.eden.data.duplicate.builder.DuplicateFilterBuilder;

/**
 * 布隆过滤器构建
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class BloomFilterBuilder extends AbstractDuplicateFilterBuilder implements DuplicateFilterBuilder {

	/**
	 * 构建数据去重过滤器
	 *
	 * @return 数据去重过滤器
	 */
	@Override
	public DuplicateFilter build() {
		return null;
	}
}
