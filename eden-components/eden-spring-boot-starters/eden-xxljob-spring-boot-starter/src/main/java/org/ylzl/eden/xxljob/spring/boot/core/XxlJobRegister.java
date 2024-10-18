package org.ylzl.eden.xxljob.spring.boot.core;

import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import org.ylzl.eden.xxljob.spring.boot.enums.ExecutorRouteStrategy;

import java.lang.annotation.*;

/**
 * XxlJob 注册器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Target({ElementType.TYPE, ElementType.METHOD})
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
	 * 任务描述
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
