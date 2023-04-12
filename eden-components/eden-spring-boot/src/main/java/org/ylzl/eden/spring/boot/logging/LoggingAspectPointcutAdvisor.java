package org.ylzl.eden.spring.boot.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.util.PathMatcher;

/**
 * 事件审计切点声明
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class LoggingAspectPointcutAdvisor extends AbstractBeanFactoryPointcutAdvisor {

	private final PathMatcher pathMatcher;

	private final String[] packages;

	/**
	 * 获取切点
	 *
	 * @return 切点
	 */
	@Override
	public Pointcut getPointcut() {
		return new LoggingAspectPointcut(pathMatcher, packages);
	}
}
