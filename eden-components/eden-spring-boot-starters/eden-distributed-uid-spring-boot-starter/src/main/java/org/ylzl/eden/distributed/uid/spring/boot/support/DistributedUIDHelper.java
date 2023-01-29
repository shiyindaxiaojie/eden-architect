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

package org.ylzl.eden.distributed.uid.spring.boot.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.distributed.uid.DistributedUID;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;

import java.util.Map;

/**
 * 分布式唯一ID操作帮助支持
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class DistributedUIDHelper {

	private static final String UID_TYPE_NOT_FOUND = "Distributed uid type named '{}' not found";

	private final String primary;

	public DistributedUID getBean() {
		return getBean(primary);
	}

	public DistributedUID getBean(String uidType) {
		Map<String, DistributedUID> distributedUIDs = ApplicationContextHelper.getBeansOfType(DistributedUID.class);
		return distributedUIDs.values().stream()
			.filter(predicate -> predicate.uidType().equalsIgnoreCase(uidType))
			.findFirst()
			.orElseThrow(() -> new RuntimeException(UID_TYPE_NOT_FOUND));
	}
}
