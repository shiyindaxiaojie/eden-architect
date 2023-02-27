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

import java.lang.annotation.*;

/**
 * Cat.logMetricForCount 注解
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CatMetric {

	/**
	 * 指标名称
	 *
	 * @return 名称
	 */
	String name() default "";

	/**
	 * 标签键值对
	 *
	 * @return 标签键值对
	 */
	String[] tags() default {};

	/**
	 * 计数
	 *
	 * @return 计数
	 */
	int count() default 0;

	/**
	 * 数量
	 *
	 * @return 数量
	 */
	String quantity() default "";

	/**
	 * 耗时（毫秒）
	 *
	 * @return 耗时（毫秒）
	 */
	String durationInMillis() default "";

	/**
	 * 总和
	 *
	 * @return 总和
	 */
	String sum() default "";
}
