package org.ylzl.eden.spring.integration.xxljob.annotations;

import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import org.ylzl.eden.spring.integration.xxljob.enums.ExecutorRouteStrategyEnum;

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
	ExecutorRouteStrategyEnum routeStrategy() default ExecutorRouteStrategyEnum.LEAST_FREQUENTLY_USED;

	/*
	 * 任务执行超时（秒）
	 */
	int timeout() default 3000;
}
