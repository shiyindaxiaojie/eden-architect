package org.ylzl.eden.distributed.uid.core;

import lombok.RequiredArgsConstructor;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;
import org.ylzl.eden.spring.framework.error.ClientAssert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分布式唯一ID实例工厂
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class DistributedUIDFactory {

	private static final Map<String, String> beanSettings = new ConcurrentHashMap<>();

	private final String defaultType;

	public static void addBean(String type, String beanName) {
		beanSettings.put(type, beanName);
	}

	public DistributedUID getExecutor() {
		DistributedUID distributedUID = StringUtils.isNotBlank(defaultType)?
			ApplicationContextHelper.getBean(beanSettings.get(defaultType.toUpperCase())) :
			ApplicationContextHelper.getBean(DistributedUID.class);
		ClientAssert.notNull(distributedUID, "B0001", "DistributedUID beanDefinition not found");
		return distributedUID;
	}

	public DistributedUID getExecutor(String type) {
		String beanName = beanSettings.get(type.toUpperCase());
		DistributedUID distributedUID = ApplicationContextHelper.getBean(beanName, DistributedUID.class);
		ClientAssert.notNull(distributedUID, "B0001", "DistributedUID beanDefinition named '" + beanName + "' not found");
		return distributedUID;
	}
}
