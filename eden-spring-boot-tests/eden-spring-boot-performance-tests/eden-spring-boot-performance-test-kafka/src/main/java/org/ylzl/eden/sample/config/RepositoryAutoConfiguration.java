package org.ylzl.eden.sample.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.ylzl.eden.spring.boot.data.audit.EnableAuditorAware;

/**
 * 数据仓库自动配置
 *
 * @author gyl
 * @since 1.0.0
 */
@EnableAuditorAware
@EnableJpaRepositories(RepositoryAutoConfiguration.JPA_PACKAGE)
@EnableTransactionManagement
@Slf4j
@Configuration
public class RepositoryAutoConfiguration {

  /** JPA 包路径 */
  public static final String JPA_PACKAGE = ApplicationConstants.BASE_PACKAGE + ".repository";
}
