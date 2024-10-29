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

package org.ylzl.eden.data.differ.integration.objectdiffer;

import de.danielbechler.diff.ObjectDiffer;
import de.danielbechler.diff.ObjectDifferBuilder;
import org.ylzl.eden.data.differ.DataDiffer;
import org.ylzl.eden.data.differ.builder.AbstractDataDifferBuilder;

/**
 * JavaObjectDiff 数据比对构建器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class ObjectDifferDataDifferBuilder extends AbstractDataDifferBuilder {

	/**
	 * 构建数据比对实例
	 *
	 * @return 数据比对实例
	 */
	@Override
	public DataDiffer build() {
		ObjectDiffer objectDiffer = ObjectDifferBuilder.startBuilding().build();
		return new ObjectDifferDataDiffer(objectDiffer);
	}
}
