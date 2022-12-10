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

package org.ylzl.eden.spring.integration.xxljob.core;

import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import org.ylzl.eden.spring.integration.xxljob.enums.ExecutorRouteStrategy;

import java.lang.annotation.*;

/**
 * XxlJob 注册器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface XxlJobRegister {

	/**
	 * 任务执行表达式
	 *
	 * @return
	 */
	String cron() default "";

	/**
	 *
	 *
	 * @return
	 */
	String author() default "xxl-job";

	/*
	 * 报警邮件
	 *
	 * @return
	 */
	String alarmEmail() default "";

	/*
	 * 执行器描述
	 */
	String desc() default "";

	/*
	 * 执行参数
	 */
	String param() default "";

	/*
	 * 失败重试次数
	 */
	int failRetryCount() default 3;

	/*
	 * 阻塞处理策略
	 */
	ExecutorBlockStrategyEnum blockStrategy() default ExecutorBlockStrategyEnum.COVER_EARLY;

	/*
	 * 执行器路由策略
	 */
	ExecutorRouteStrategy routeStrategy() default ExecutorRouteStrategy.LEAST_FREQUENTLY_USED;

	/*
	 * 任务执行超时（秒）
	 */
	int timeout() default 3000;
}
