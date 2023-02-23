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

package org.ylzl.eden.spring.integration.cat;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.ylzl.eden.spring.integration.cat.config.CatImportSelector;

import java.lang.annotation.*;

/**
 * 开启 CAT 埋点
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({CatImportSelector.class})
public @interface EnableCat {

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
	 */
	int order() default Ordered.LOWEST_PRECEDENCE;
}
