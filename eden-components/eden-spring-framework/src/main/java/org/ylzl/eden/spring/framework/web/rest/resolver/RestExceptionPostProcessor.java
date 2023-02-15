package org.ylzl.eden.spring.framework.web.rest.resolver;

import org.ylzl.eden.extension.SPI;

/**
 * Rest 异常后置处理
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@SPI
public interface RestExceptionPostProcessor {

	void postProcess(Throwable e);
}
