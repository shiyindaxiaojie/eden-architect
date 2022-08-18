package org.ylzl.eden.spring.boot.cat.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.integration.cat.integration.mybatis.interceptor.MybatisCatInterceptor;

import java.util.List;

/**
 * Mybatis 集成 CAT 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnBean({
	CatAutoConfiguration.class,
	SqlSessionFactory.class
})
@RequiredArgsConstructor
@Slf4j
@Configuration
public class MybatisCatAutoConfiguration implements InitializingBean {

	private final List<SqlSessionFactory> sqlSessionFactories;

	@Override
	public void afterPropertiesSet() {
		for (SqlSessionFactory sqlSessionFactory : sqlSessionFactories) {
			if (sqlSessionFactory != null) {
				sqlSessionFactory.getConfiguration().addInterceptor(new MybatisCatInterceptor());
			}
		}
	}
}
