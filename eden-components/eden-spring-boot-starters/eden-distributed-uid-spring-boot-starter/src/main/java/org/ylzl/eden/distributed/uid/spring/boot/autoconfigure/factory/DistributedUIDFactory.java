package org.ylzl.eden.distributed.uid.spring.boot.autoconfigure.factory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.distributed.uid.core.DistributedUID;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;
import org.ylzl.eden.spring.framework.error.util.AssertUtils;

import java.text.MessageFormat;

/**
 * 分布式唯一ID操作实例工厂
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class DistributedUIDFactory {

	private static final String BEAN_DEFINITION_NOT_FOUND = "DistributedUID beanDefinition not found";

	private static final String BEAN_DEFINITION_NOT_FOUND_BY = "DistributedUID beanDefinition named '{}' not found by '{}'";

	private final DistributedUIDBeanType defaultType;

	public DistributedUID getExecutor() {
		DistributedUID distributedLock = ApplicationContextHelper.getBean(defaultType.getUidName(), DistributedUID.class);
		AssertUtils.notNull(distributedLock, "SYS-ERROR-500", BEAN_DEFINITION_NOT_FOUND);
		return distributedLock;
	}

	public DistributedUID getExecutor(DistributedUIDBeanType distributedUIDBeanType) {
		String beanName = distributedUIDBeanType.getUidName();
		DistributedUID distributedUID = ApplicationContextHelper.getBean(beanName, DistributedUID.class);
		AssertUtils.notNull(distributedUID, "SYS-ERROR-500", MessageFormat.format(BEAN_DEFINITION_NOT_FOUND_BY, beanName));
		return distributedUID;
	}
}
