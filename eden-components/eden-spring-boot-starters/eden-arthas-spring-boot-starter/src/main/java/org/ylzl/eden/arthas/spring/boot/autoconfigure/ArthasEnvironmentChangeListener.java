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

package org.ylzl.eden.arthas.spring.boot.autoconfigure;

import com.alibaba.arthas.spring.ArthasProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.ylzl.eden.arthas.spring.boot.attach.CustomArthasAgent;
import org.ylzl.eden.arthas.spring.boot.env.SpringArthasProperties;

import java.util.Map;
import java.util.Set;

/**
 * Arthas 开关监听器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class ArthasEnvironmentChangeListener implements ApplicationListener<EnvironmentChangeEvent> {

	public static final String ARTHAS_AGENT = "arthasAgent";

	public static final String REGISTER_ARTHAS = "Register arthas to {}";

	public static final String DESTROY_ARTHAS = "Destroy arthas from {}";

	private final ApplicationContext applicationContext;

	private final Environment environment;

	private final ArthasProperties arthasProperties;

	private final Map<String, String> arthasConfigMap;

	@Override
	public void onApplicationEvent(EnvironmentChangeEvent event) {
		Set<String> keys = event.getKeys();
		for (String key : keys) {
			if (SpringArthasProperties.ENABLED.equals(key)) {
				if (Boolean.parseBoolean(environment.getProperty(key))) {
					registerBean();
				} else {
					destroyBean();
				}
				break;
			}
		}
	}

	private void registerBean() {
		log.info(REGISTER_ARTHAS, arthasProperties.getTunnelServer());
		DefaultListableBeanFactory defaultListableBeanFactory = getDefaultListableBeanFactory();
		if (defaultListableBeanFactory.containsBean(ARTHAS_AGENT)) {
			((CustomArthasAgent) defaultListableBeanFactory.getBean(ARTHAS_AGENT)).init();
		} else {
			defaultListableBeanFactory.registerSingleton(ARTHAS_AGENT,
				ArthasAutoConfiguration.init(arthasConfigMap, environment, arthasProperties));
		}
	}

	private void destroyBean() {
		log.info(DESTROY_ARTHAS, arthasProperties.getTunnelServer());
		DefaultListableBeanFactory defaultListableBeanFactory = getDefaultListableBeanFactory();
		if (defaultListableBeanFactory.containsBean(ARTHAS_AGENT)) {
			((CustomArthasAgent) defaultListableBeanFactory.getBean(ARTHAS_AGENT)).destroy();
			defaultListableBeanFactory.destroySingleton(ARTHAS_AGENT);
		}
	}

	@NotNull
	private DefaultListableBeanFactory getDefaultListableBeanFactory() {
		return (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
	}
}
