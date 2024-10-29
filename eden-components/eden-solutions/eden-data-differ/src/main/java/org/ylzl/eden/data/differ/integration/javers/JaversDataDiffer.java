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

package org.ylzl.eden.data.differ.integration.javers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.javers.core.Javers;
import org.ylzl.eden.commons.collections.CollectionUtils;
import org.ylzl.eden.data.differ.DataDiffer;
import org.ylzl.eden.data.differ.Change;
import org.ylzl.eden.data.differ.ChangeType;
import org.ylzl.eden.data.differ.Diff;

import java.util.Collection;

/**
 * Javers 数据比对
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
@RequiredArgsConstructor
public class JaversDataDiffer implements DataDiffer {

	private final Javers javers;

	/**
	 * 获取实际比对工具
	 *
	 * @return 比对工具
	 */
	@Override
	public Object getDiffer() {
		return javers;
	}

	/**
	 * 单个比对
	 *
	 * @param oldVersion     历史版本
	 * @param currentVersion 当前版本
	 * @return 比对结果
	 */
	@Override
	public Diff compare(Object oldVersion, Object currentVersion) {
		return convert(javers.compare(oldVersion, currentVersion));
	}

	/**
	 * 集合比对
	 *
	 * @param oldVersion     历史版本
	 * @param currentVersion 当前版本
	 * @param itemClass      比对类
	 * @return 比对结果
	 */
	@Override
	public <T> Diff compareCollections(Collection<T> oldVersion, Collection<T> currentVersion, Class<T> itemClass) {
		return convert(javers.compareCollections(oldVersion, currentVersion, itemClass));
	}

	/**
	 * 转换比对结果
	 *
	 * @param javersDiff 第三方比对结果
	 * @return 统一比对结果
	 */
	private Diff convert(org.javers.core.diff.Diff javersDiff) {
		Diff diff = new Diff();
		boolean hasChanges = javersDiff.hasChanges();
		diff.setHasChanges(hasChanges);
		if (!hasChanges) {
			return diff;
		}

		javersDiff.groupByObject().forEach(group -> {
			if (CollectionUtils.isNotEmpty(group.getNewObjects())) {
				group.getNewObjects().forEach(newObject -> {
					diff.addChange(new Change(newObject.getAffectedGlobalId().value(),
						newObject.getAffectedObject().get(), ChangeType.NEW));
				});
			}
			if (CollectionUtils.isNotEmpty(group.getPropertyChanges())) {
				group.getPropertyChanges().forEach(propertyChange -> {
					diff.addChange(new Change(propertyChange.getAffectedGlobalId().value(),
						propertyChange.getAffectedObject().get(), ChangeType.MODIFIED));
				});
			}
			if (CollectionUtils.isNotEmpty(group.getObjectsRemoved())) {
				group.getObjectsRemoved().forEach(objectRemoved -> {
					diff.addChange(new Change(objectRemoved.getAffectedGlobalId().value(),
						objectRemoved.getAffectedObject(), ChangeType.REMOVED));
				});
			}
		});
		return diff;
	}
}
