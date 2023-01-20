/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.event.auditor;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.ylzl.eden.event.auditor.config.EventAuditorImportSelector;

import java.lang.annotation.*;

/**
 * 开启事件审计
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Import(EventAuditorImportSelector.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface EnableEventAuditor {

	/**
	 * 是否开启 CGLIB 代理
	 *
	 * @return 是否开启 CGLIB 代理
	 */
	boolean proxyTargetClass() default false;

	/**
	 * PointcutAdvisor 代理模式
	 *
	 * @return 代理模式
	 * @see org.springframework.context.annotation.AdviceModeImportSelector
	 */
	AdviceMode mode() default AdviceMode.PROXY;

	/**
	 * PointcutAdvisor 优先级
	 *
	 * @return 优先级
	 * @see org.ylzl.eden.event.auditor.aop.EventAuditorPointcutAdvisor
	 */
	int order() default Ordered.LOWEST_PRECEDENCE;
}
