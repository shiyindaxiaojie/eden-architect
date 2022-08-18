package org.ylzl.eden.spring.integration.consistencytask.annotation;

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
