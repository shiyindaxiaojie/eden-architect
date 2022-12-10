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

package org.ylzl.eden.spring.boot.info.autoconfigure;

import org.springframework.boot.actuate.autoconfigure.info.ConditionalOnEnabledInfoContributor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.ylzl.eden.spring.boot.info.contributor.ActiveProfilesInfoContributor;
import org.ylzl.eden.spring.boot.info.contributor.InfoContributorProvider;
import org.ylzl.eden.spring.boot.info.contributor.MailEnabledInfoContributor;

/**
 * InfoContributor 自动装配
 *
 * <p>从 Spring Boot 1.X 升级到 2.X
 *
 * <ul>
 *   <li>org.springframework.boot.actuate.autoconfigure.InfoContributorAutoConfiguration 迁移到 {@link
 *       InfoContributorAutoConfiguration}
 *   <li>org.springframework.boot.actuate.autoconfigure.ConditionalOnEnabledInfoContributor 迁移到
 *       {@link ConditionalOnEnabledInfoContributor}
 * </ul>
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@AutoConfigureAfter(org.springframework.boot.actuate.autoconfigure.info.InfoContributorAutoConfiguration.class)
@Configuration(proxyBeanMethods = false, value = "enhancedInfoContributorAutoConfiguration")
public class InfoContributorAutoConfiguration {

	@ConditionalOnEnabledInfoContributor("management.info.active-profiles.enabled")
	@Bean
	public ActiveProfilesInfoContributor activeProfilesInfoContributor(
		ConfigurableEnvironment environment) {
		return new ActiveProfilesInfoContributor(environment);
	}

	@ConditionalOnEnabledInfoContributor("management.info.mail.enabled")
	@Bean
	public MailEnabledInfoContributor mailEnabledInfoContributor() {
		return new MailEnabledInfoContributor();
	}

	@ConditionalOnBean(BuildProperties.class)
	@Bean
	public InfoContributorProvider infoContributorProvider(BuildProperties buildProperties) {
		return new InfoContributorProvider(buildProperties);
	}
}
