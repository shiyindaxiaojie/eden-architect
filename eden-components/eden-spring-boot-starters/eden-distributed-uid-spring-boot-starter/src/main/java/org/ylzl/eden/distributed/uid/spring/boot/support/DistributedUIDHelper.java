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
import org.ylzl.eden.spring.framework.error.util.AssertUtils;

import java.text.MessageFormat;

/**
 * 分布式唯一ID操作帮助支持
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class DistributedUIDHelper {

	private static final String BEAN_DEFINITION_NOT_FOUND = "DistributedUID bean definition named '{}' not found";

	private final DistributedUIDBeanNames primary;

	public DistributedUID getBean() {
		DistributedUID distributedLock = ApplicationContextHelper.getBean(primary.getBeanName(), DistributedUID.class);
		AssertUtils.notNull(distributedLock, "SYS-ERROR-500", BEAN_DEFINITION_NOT_FOUND, primary.getBeanName());
		return distributedLock;
	}

	public DistributedUID getBean(DistributedUIDBeanNames distributedUIDBeanNames) {
		String beanName = distributedUIDBeanNames.getBeanName();
		DistributedUID distributedUID = ApplicationContextHelper.getBean(beanName, DistributedUID.class);
		AssertUtils.notNull(distributedUID, "SYS-ERROR-500", MessageFormat.format(BEAN_DEFINITION_NOT_FOUND, beanName));
		return distributedUID;
	}
}
