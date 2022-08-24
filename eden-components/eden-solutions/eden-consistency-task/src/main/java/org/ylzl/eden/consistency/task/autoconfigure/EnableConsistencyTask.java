package org.ylzl.eden.consistency.task.autoconfigure;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启一致性任务
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
@Import({ConsistencyTaskSelector.class})
public @interface EnableConsistencyTask {


}
