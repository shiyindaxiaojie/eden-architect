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

package org.ylzl.eden.data.auditor.integration.differ.javers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.javers.core.Changes;
import org.javers.core.Javers;
import org.javers.core.diff.changetype.NewObject;
import org.javers.core.diff.changetype.ObjectRemoved;
import org.javers.core.diff.changetype.ValueChange;
import org.ylzl.eden.data.auditor.DataDiffer;
import org.ylzl.eden.data.auditor.differ.Change;
import org.ylzl.eden.data.auditor.differ.ChangeType;
import org.ylzl.eden.data.auditor.differ.Diff;

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
	 * @param oldVersion 历史版本
	 * @param currentVersion 当前版本
	 * @return 比对结果
	 */
	@Override
	public Diff compare(Object oldVersion, Object currentVersion) {
		org.javers.core.Changes changes = javers.compare(oldVersion, currentVersion).getChanges();
		return convert(changes);
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
		org.javers.core.Changes changes = javers.compareCollections(oldVersion, currentVersion, itemClass).getChanges();
		return convert(changes);
	}

	/**
	 * 转换比对结果
	 *
	 * @param changes 第三方比对结果
	 * @return 统一比对结果
	 */
	private Diff convert(Changes changes) {
		Diff diff = new Diff();
		changes.forEach(change -> change.getAffectedObject().ifPresent(value -> {
			ChangeType changeType = null;
			if (change instanceof NewObject) {
				changeType = ChangeType.NEW;
			} else if (change instanceof ValueChange) {
				changeType = ChangeType.MODIFIED;
			} else if (change instanceof ObjectRemoved) {
				changeType = ChangeType.REMOVED;
			}
			diff.addChange(new Change(change.getAffectedLocalId().toString(), value, changeType));
		}));
		return diff;
	}
}
