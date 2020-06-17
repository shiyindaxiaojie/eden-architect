package org.ylzl.eden.sample.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * 数据仓库自动配置
 *
 * @author gyl
 * @since 1.0.0
 */
@EnableRedisRepositories(basePackages = RepositoryAutoConfiguration.REDIS_PACKAGE)
@Slf4j
@Configuration
public class RepositoryAutoConfiguration {

  public static final String REDIS_PACKAGE = ApplicationConstants.BASE_PACKAGE + ".domain";
}
