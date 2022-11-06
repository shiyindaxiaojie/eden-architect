package org.ylzl.eden.distributed.lock.spring.boot.autoconfigure.factory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.distributed.lock.core.DistributedLock;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;
import org.ylzl.eden.spring.framework.error.util.AssertUtils;

import java.text.MessageFormat;

/**
 * 分布式锁操作实例工厂
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class DistributedLockFactory {

	private static final String BEAN_DEFINITION_NOT_FOUND = "DistributedLock beanDefinition not found";

	private static final String BEAN_DEFINITION_NOT_FOUND_BY = "DistributedLock beanDefinition named '{}' not found by '{}'";

	private final DistributedLockBeanType defaultType;

	public DistributedLock getExecutor() {
		DistributedLock distributedLock = ApplicationContextHelper.getBean(defaultType.getLockName(), DistributedLock.class);
		AssertUtils.notNull(distributedLock, "SYS-ERROR-500", BEAN_DEFINITION_NOT_FOUND);
		return distributedLock;
	}

	public DistributedLock getExecutor(DistributedLockBeanType distributedLockBeanType) {
		String beanName = distributedLockBeanType.getLockName();
		DistributedLock distributedLock = ApplicationContextHelper.getBean(beanName, DistributedLock.class);
		AssertUtils.notNull(distributedLock, "SYS-ERROR-500", MessageFormat.format(BEAN_DEFINITION_NOT_FOUND_BY, beanName));
		return distributedLock;
	}
}
