package org.ylzl.eden.spring.boot.mybatis.autoconfigure;

import com.github.pagehelper.PageInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Mybatis 分页插件自动装配
 *
 * @author gyl
 * @since 1.0.0
 */
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties({MybatisProperties.class})
@ConditionalOnClass({PageInterceptor.class})
@Slf4j
@Configuration
public class MybatisPageHelperAutoConfiguration {

	public static final String AUTOWIRED_PAGE_HELPER = "Autowired PageHelper";

	@ConditionalOnMissingBean
	@Bean
	public PageInterceptor pageInterceptor() {
		log.debug(AUTOWIRED_PAGE_HELPER);
		PageInterceptor pageInterceptor = new PageInterceptor();
		Properties properties = new Properties();
		properties.setProperty("supportMethodsArguments", "true");
		properties.setProperty("params", "count=countSql");
		pageInterceptor.setProperties(properties);
		return pageInterceptor;
	}
}
