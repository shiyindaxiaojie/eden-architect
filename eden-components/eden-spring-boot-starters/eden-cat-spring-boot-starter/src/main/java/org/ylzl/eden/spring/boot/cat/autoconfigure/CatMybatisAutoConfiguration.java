package org.ylzl.eden.spring.boot.cat.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.boot.cat.integration.mybatis.CatMybatisInterceptor;

import java.util.List;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@ConditionalOnBean(SqlSessionFactory.class)
@Slf4j
@Configuration
public class CatMybatisAutoConfiguration implements InitializingBean {

	private final List<SqlSessionFactory> sqlSessionFactories;

	@Override
	public void afterPropertiesSet() throws Exception {
		for (SqlSessionFactory sqlSessionFactory : sqlSessionFactories) {
			if (sqlSessionFactory != null) {
				sqlSessionFactory.getConfiguration().addInterceptor(new CatMybatisInterceptor());
			}
		}
	}
}
