package org.ylzl.eden.spring.framework.cola.catchlog.autoconfigure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.framework.cola.catchlog.aop.CatchLogAspect;

/**
 * 日志切面自动装配
 *
 * @author gyl
 * @since 2.4.x
 */
@ComponentScan(basePackageClasses = CatchLogAspect.class)
//@Slf4j
@Configuration
public class CatchLogAutoConfiguration {
//  FIXME: 使用 Bean 声明会产生循环依赖
//	public static final String AUTOWIRED_CATCH_LOG_ASPECT = "Autowired CatchLogAspect";
//
//	@ConditionalOnMissingBean(CatchLogAspect.class)
//	@Bean
//	public CatchLogAspect catchLogAspect() {
//		log.debug(AUTOWIRED_CATCH_LOG_ASPECT);
//		return new CatchLogAspect();
//	}
}
