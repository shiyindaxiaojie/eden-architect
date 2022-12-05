package org.ylzl.eden.distributed.uid.spring.boot.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.distributed.uid.core.DistributedUID;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;
import org.ylzl.eden.spring.framework.error.util.AssertUtils;

import java.text.MessageFormat;

/**
 * 分布式唯一ID操作助手
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
