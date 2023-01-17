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

import com.dianping.cat.Cat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;
import org.springframework.core.env.Environment;
import org.ylzl.eden.cat.spring.boot.env.CatProperties;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;
import org.ylzl.eden.spring.framework.bootstrap.constant.SpringProperties;
import org.ylzl.eden.spring.framework.error.util.AssertUtils;
import org.ylzl.eden.spring.integration.cat.autoconfigure.CatAnnotationProcessorRegister;

/**
 * CAT 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = CatProperties.PREFIX,
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE,
	matchIfMissing = true
)
@ConditionalOnClass(Cat.class)
@Import(CatAnnotationProcessorRegister.class)
@EnableConfigurationProperties(CatProperties.class)
@RequiredArgsConstructor
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class CatAutoConfiguration implements InitializingBean {

	private static final String INITIALIZING_CAT_CLIENT = "Initializing cat client";

	private static final String CAT_HOME = "CAT_HOME";

	private final CatProperties catProperties;

	private final Environment environment;

	@Override
	public void afterPropertiesSet() {
		log.debug(INITIALIZING_CAT_CLIENT);

		String servers = catProperties.getServers();
		AssertUtils.notNull(servers, "cat servers is not null");

		// 动态设置 cat-home 路径
		System.setProperty(CAT_HOME, catProperties.getHome());

		// 代替 META-INF/app.properites
		String domain;
		if (StringUtils.isBlank(catProperties.getDomain())) {
			domain = environment.getProperty(SpringProperties.SPRING_APPLICATION_NAME);
		} else {
			domain = catProperties.getDomain();
		}

		Cat.initializeByDomain(domain,
			catProperties.getTcpPort(),
			catProperties.getHttpPort(),
			servers.split(Strings.COMMA));
	}
}
