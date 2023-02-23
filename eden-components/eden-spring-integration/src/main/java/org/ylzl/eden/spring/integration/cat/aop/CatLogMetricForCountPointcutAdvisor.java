package org.ylzl.eden.spring.integration.cat.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/**
 * CatLogMetricForCount 切点声明
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class CatLogMetricForCountPointcutAdvisor extends AbstractBeanFactoryPointcutAdvisor {

	private final CatLogMetricForCountPointcut pointcut = new CatLogMetricForCountPointcut();

	/**
	 * 获取切点
	 *
	 * @return 切点
	 */
	@Override
	public Pointcut getPointcut() {
		return pointcut;
	}
}
