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

package org.ylzl.eden.mybatis.spring.boot.autoconfigure;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.ylzl.eden.mybatis.spring.boot.env.MybatisPlusExtensionProperties;
import org.ylzl.eden.spring.data.mybatis.autofill.AutofillMetaObjectHandler;

/**
 * MybatisPlus 扩展自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@AutoConfigureAfter(MybatisPlusAutoConfiguration.class)
@ConditionalOnClass({
	SqlSessionFactory.class,
	SqlSessionFactoryBean.class,
	MybatisConfiguration.class
})
@ConditionalOnProperty(name = MybatisPlusExtensionProperties.AUTO_FILL_ENABLED, havingValue = "true")
@EnableConfigurationProperties({MybatisPlusExtensionProperties.class})
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class MybatisPlusExtensionAutoConfiguration {

	private static final String AUTOWIRED_META_OBJECT_HANDLER = "Autowired MetaObjectHandler";

	@ConditionalOnMissingBean
	@Bean
	public MetaObjectHandler metaObjectHandler(MybatisPlusExtensionProperties properties) {
		log.debug(AUTOWIRED_META_OBJECT_HANDLER);
		return new AutofillMetaObjectHandler(properties.getAutoFill().getCreatedDateFieldName(),
			properties.getAutoFill().getLastModifiedDateFieldName());
	}
}
