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
 * 审计标记
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
	 * 操作对象，支持 SpEL 表达式
	 *
	 * @return 操作对象
	 */
	String operator() default Strings.EMPTY;

	/**
	 * 操作角色，支持 SpEL 表达式
	 * <br/> 用于区分运营人员和用户
	 *
	 * @return 操作角色
	 */
	String role() default Strings.EMPTY;

	/**
	 * 业务场景，支持 SpEL 表达式
	 * <br/> 推荐风格：产品线+用例+场景
	 *
	 * @return 业务场景
	 */
	String bizScenario() default Strings.EMPTY;

	/**
	 * 记录内容
	 *
	 * @return 记录内容，支持 SpEL 表达式
	 */
	String content() default Strings.EMPTY;

	/**
	 * 额外信息
	 * <br/> 防止记录的内容过长无法展示，额外序列化保存
	 *
	 * @return 额外信息，支持 SpEL 表达式
	 */
	String extra() default Strings.EMPTY;

	/**
	 * 触发条件
	 *
	 * @return 触发条件，为空表示默认触发
	 */
	String condition() default Strings.EMPTY;

	/**
	 * 调用方法之前解析 SpEL 参数
	 * <br/>
	 *
	 * @return 是否提前解析
	 */
	boolean evalBeforeInvoke() default false;

	/**
	 * 是否记录返回值
	 * <br/> 防止记录查询类返回的 List 大对象导致 OOM
	 *
	 * @return 是否记录
	 */
	boolean recordReturnValue() default false;
}
