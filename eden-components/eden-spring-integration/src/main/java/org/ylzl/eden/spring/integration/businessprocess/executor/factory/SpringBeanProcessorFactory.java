package org.ylzl.eden.spring.integration.businessprocess.executor.factory;

import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;
import org.ylzl.eden.spring.integration.businessprocess.executor.Processor;

/**
 * 基于 Spring Bean 机制实例化流程工厂
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class SpringBeanProcessorFactory implements ProcessorFactory {

	/**
	 * 实例化
	 *
	 * @param className   类名
	 * @param processName 名称
	 * @return
	 * @throws Exception
	 */
	@Override
	public Processor newInstance(String className, String processName) throws Exception {
		Class<?> clazz = Class.forName(className);
		Object bean = ApplicationContextHelper.getBean(clazz);
		if (!(bean instanceof Processor)) {
			throw new IllegalArgumentException("Spring Bean '" + className + "' is not instance of Processor");
		}

		Processor processor = (Processor) bean;
		processor.setName(processName);
		return processor;
	}
}
