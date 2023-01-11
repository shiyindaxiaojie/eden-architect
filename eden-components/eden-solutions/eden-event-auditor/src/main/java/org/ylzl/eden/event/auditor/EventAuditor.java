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

import org.ylzl.eden.commons.lang.Strings;

import java.lang.annotation.*;

/**
 * 事件审计
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface EventAuditor {

	/**
	 * @return 审计对象
	 */
	String auditor() default Strings.EMPTY;

	/**
	 * @return 业务
	 */
	String bizId() default Strings.EMPTY;

	/**
	 * @return 用例
	 */
	String useCase() default Strings.EMPTY;

	/**
	 * @return 场景
	 */
	String scenario() default Strings.EMPTY;

	/**
	 * @return 方法执行成功后的记录内容，支持 SpEL 表达式
	 */
	String onSuccess();

	/**
	 * @return 方法执行失败后的记录内容，支持 SpEL 表达式
	 */
	String onFail() default Strings.EMPTY;

	/**
	 * @return 触发条件，为空表示默认触发
	 */
	String condition() default Strings.EMPTY;
}
