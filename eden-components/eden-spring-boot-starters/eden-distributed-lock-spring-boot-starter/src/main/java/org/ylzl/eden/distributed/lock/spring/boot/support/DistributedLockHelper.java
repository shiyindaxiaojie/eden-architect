package org.ylzl.eden.distributed.lock.spring.boot.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.distributed.lock.core.DistributedLock;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;
import org.ylzl.eden.spring.framework.error.util.AssertUtils;

import java.text.MessageFormat;

/**
 * 分布式锁操作实例助手
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class DistributedLockHelper {

	private static final String BEAN_DEFINITION_NOT_FOUND = "DistributedLock bean definition named '{}' not found";

	private final DistributedLockBeanNames primary;

	public DistributedLock getBean() {
		DistributedLock distributedLock = ApplicationContextHelper.getBean(primary.getBeanName(), DistributedLock.class);
		AssertUtils.notNull(distributedLock, "SYS-ERROR-500", BEAN_DEFINITION_NOT_FOUND, primary.getBeanName());
		return distributedLock;
	}

	public DistributedLock getBean(DistributedLockBeanNames distributedLockBeanNames) {
		String beanName = distributedLockBeanNames.getBeanName();
		DistributedLock distributedLock = ApplicationContextHelper.getBean(beanName, DistributedLock.class);
		AssertUtils.notNull(distributedLock, "SYS-ERROR-500", MessageFormat.format(BEAN_DEFINITION_NOT_FOUND, beanName));
		return distributedLock;
	}
}
