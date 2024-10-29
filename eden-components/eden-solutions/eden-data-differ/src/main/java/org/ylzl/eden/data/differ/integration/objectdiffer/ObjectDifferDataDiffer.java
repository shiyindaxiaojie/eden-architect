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
import de.danielbechler.diff.node.DiffNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.ylzl.eden.data.differ.DataDiffer;
import org.ylzl.eden.data.differ.Diff;

import java.util.Collection;

/**
 * JavaObjectDiff 数据比对
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
@RequiredArgsConstructor
public class ObjectDifferDataDiffer implements DataDiffer {

	private final ObjectDiffer objectDiffer;

	/**
	 * 获取实际比对工具
	 *
	 * @return 比对工具
	 */
	@Override
	public Object getDiffer() {
		return objectDiffer;
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
		return convert(objectDiffer.compare(currentVersion, oldVersion));
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
		return convert(objectDiffer.compare(currentVersion, oldVersion));
	}

	/**
	 * 转换比对结果
	 *
	 * @param diffNode 第三方比对结果
	 * @return 统一比对结果
	 */
	private <T> Diff convert(DiffNode diffNode) {
		Diff diff = new Diff();
		boolean hasChanges = diffNode.hasChanges();
		diff.setHasChanges(hasChanges);
		if (!hasChanges) {
			return diff;
		}

		// TODO
		/*diffNode.visit((node, visit) -> {
			if (node.isAdded()) {
				diff.addChange(new Change(node.get(), ChangeType.NEW));
			}
			if (node.isChanged()) {
				diff.addChange(new Change(node.get(), ChangeType.MODIFIED));
			}
			if (node.isRemoved()) {
				diff.addChange(new Change(node.get(), ChangeType.REMOVED));
			}
		});*/
		return diff;
	}
}
