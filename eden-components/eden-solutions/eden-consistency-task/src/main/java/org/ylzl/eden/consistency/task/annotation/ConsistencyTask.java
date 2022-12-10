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

package org.ylzl.eden.consistency.task.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 一致性任务
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ConsistencyTask {

	/**
	 * 任务ID
	 *
	 * @return
	 */
	@AliasFor("id")
	String[] value() default "";

	/**
	 * 任务ID
	 *
	 * @return
	 */
	@AliasFor("value")
	String id() default "";

	/**
	 * 执行间隔
	 *
	 * @return
	 */
	int executeIntervalInSeconds() default 60;

	/**
	 * 延迟时间
	 *
	 * @return
	 */
	int delayInSeconds() default 60;

	/**
	 * 告警表达式
	 *
	 * @return
	 */
	String alertExpression() default "executeTimes > 1 && executeTimes < 5";

	/**
	 * 告警执行的 Spring Bean
	 *
	 * @return
	 */
	String alertSpringBeanName() default "";

	/**
	 * 指定降级类
	 *
	 * @return
	 */
	Class<?> fallbackClass() default void.class;

	/**
	 * 是否调度执行
	 */
	boolean isScheduleExecute() default false;

	/**
	 * 是否异步执行
	 */
	boolean isAsyncExecute() default true;
}
