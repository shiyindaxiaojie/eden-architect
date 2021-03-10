package org.ylzl.eden.spring.boot.framework.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.boot.framework.aop.timer.TimerAspect;

/**
 * 耗时统计切面自动配置类
 *
 * @author gyl
 * @since 0.0.1
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
