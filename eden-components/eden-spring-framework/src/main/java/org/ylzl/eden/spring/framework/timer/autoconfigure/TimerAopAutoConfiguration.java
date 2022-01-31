package org.ylzl.eden.spring.framework.timer.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.framework.timer.TimerAspect;

/**
 * 耗时统计切面自动装配类
 *
 * @author gyl
 * @since 2.4.x
 */
@Slf4j
@Configuration
public class TimerAopAutoConfiguration {

	@ConditionalOnMissingBean
	@Bean
	public TimerAspect timerAspect() {
		return new TimerAspect();
	}
}
