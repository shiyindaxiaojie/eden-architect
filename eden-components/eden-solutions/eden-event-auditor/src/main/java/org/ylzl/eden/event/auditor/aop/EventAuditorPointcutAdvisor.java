package org.ylzl.eden.event.auditor.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/**
 * 事件审计切点声明
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class EventAuditorPointcutAdvisor extends AbstractBeanFactoryPointcutAdvisor {

	private final EventAuditorPointcut pointcut = new EventAuditorPointcut();

	@Override
	public Pointcut getPointcut() {
		return pointcut;
	}
}
