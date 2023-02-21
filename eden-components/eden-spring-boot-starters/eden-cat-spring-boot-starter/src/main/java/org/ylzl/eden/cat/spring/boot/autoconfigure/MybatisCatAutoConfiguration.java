/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.cat.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.ylzl.eden.spring.integration.cat.integration.mybatis.MybatisCatInterceptor;

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
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
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
