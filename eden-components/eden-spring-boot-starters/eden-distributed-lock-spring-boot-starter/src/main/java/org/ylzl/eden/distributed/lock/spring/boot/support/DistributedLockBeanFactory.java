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

package org.ylzl.eden.distributed.lock.spring.boot.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.distributed.lock.DistributedLock;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;

import java.util.Map;

/**
 * 分布式锁操作实例工厂类
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class DistributedLockBeanFactory {

	private static final String LOCK_TYPE_NOT_FOUND = "Distributed lock type named '{}' not found";

	private final String primary;

	public DistributedLock getBean() {
		return getBean(primary);
	}

	public DistributedLock getBean(String lockType) {
		Map<String, DistributedLock> distributedLocks = ApplicationContextHelper.getBeansOfType(DistributedLock.class);
		return distributedLocks.values().stream()
			.filter(predicate -> predicate.lockType().equalsIgnoreCase(lockType))
			.findFirst()
			.orElseThrow(() -> new RuntimeException(LOCK_TYPE_NOT_FOUND));
	}
}
