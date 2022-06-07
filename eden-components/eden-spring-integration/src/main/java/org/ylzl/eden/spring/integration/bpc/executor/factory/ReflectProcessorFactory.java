package org.ylzl.eden.spring.integration.bpc.executor.factory;

import org.ylzl.eden.spring.integration.bpc.executor.Processor;

/**
 * 基于 JDK 反射机制实例化流程工厂
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class ReflectProcessorFactory implements ProcessorFactory {

	/**
	 * 实例化
	 *
	 * @param className 类名
	 * @param name      名称
	 * @return
	 * @throws Exception
	 */
	@Override
	public Processor newInstance(String className, String name) throws Exception {
		Class<?> clazz = Class.forName(className);
		Object obj = clazz.newInstance();
		if (!(obj instanceof Processor)) {
			throw new IllegalArgumentException("Class '" + className + "' is not instance of Processor");
		}

		Processor processor = (Processor) obj;
		processor.setName(name);
		return (Processor) obj;
	}
}
