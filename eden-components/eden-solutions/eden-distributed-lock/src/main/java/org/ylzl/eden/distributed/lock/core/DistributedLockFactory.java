package org.ylzl.eden.distributed.lock.core;

import lombok.RequiredArgsConstructor;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;
import org.ylzl.eden.spring.framework.error.ClientAssert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分布式锁操作实例工厂
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class DistributedLockFactory {

	private static final Map<String, String> beanSettings = new ConcurrentHashMap<>();

	private final String defaultType;

	public static void addBean(String type, String beanName) {
		beanSettings.put(type, beanName);
	}

	public DistributedLock getExecutor() {
		DistributedLock distributedLock = StringUtils.isNotBlank(defaultType)?
			ApplicationContextHelper.getBean(beanSettings.get(defaultType.toUpperCase())) :
			ApplicationContextHelper.getBean(DistributedLock.class);
		ClientAssert.notNull(distributedLock, "SYS-ERROR-500", "DistributedLock beanDefinition not found");
		return distributedLock;
	}

	public DistributedLock getExecutor(String type) {
		String beanName = beanSettings.get(type.toUpperCase());
		DistributedLock distributedLock = ApplicationContextHelper.getBean(beanName, DistributedLock.class);
		ClientAssert.notNull(distributedLock, "SYS-ERROR-500", "DistributedLock beanDefinition named '" + beanName + "' not found");
		return distributedLock;
	}
}
