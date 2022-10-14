package org.ylzl.eden.spring.boot.arthas.listener;

import com.alibaba.arthas.spring.ArthasProperties;
import com.taobao.arthas.agent.attach.ArthasAgent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.ylzl.eden.spring.boot.arthas.autoconfigure.ArthasAutoConfiguration;
import org.ylzl.eden.spring.boot.arthas.env.SpringArthasProperties;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Arthas 开关监听器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class ArthasEnvironmentChangeListener implements ApplicationListener<EnvironmentChangeEvent> {

	public static final AtomicBoolean REGISTER_STATE = new AtomicBoolean(true);

	public static final String ARTHAS_AGENT = "arthasAgent";

	public static final String REGISTER_ARTHAS = "Register arthas";

	public static final String ARTHAS_ALREADY_REGISTERED = "Arthas already registered";

	public static final String DESTROY_ARTHAS = "Destroy arthas";

	public static final String ARTHAS_ALREADY_DESTROY = "Arthas already destroy";

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
		if (!REGISTER_STATE.compareAndSet(false, true)) {
			log.info(ARTHAS_ALREADY_REGISTERED);
			return;
		}
		log.info(REGISTER_ARTHAS);
		DefaultListableBeanFactory defaultListableBeanFactory = getDefaultListableBeanFactory();
		if (defaultListableBeanFactory.containsBean(ARTHAS_AGENT)) {
			((ArthasAgent) defaultListableBeanFactory.getBean(ARTHAS_AGENT)).init();
		} else {
			defaultListableBeanFactory.registerSingleton(ARTHAS_AGENT,
				ArthasAutoConfiguration.initArthasAgent(arthasConfigMap, environment, arthasProperties));
		}
	}

	private void destroyBean() {
		if (!REGISTER_STATE.compareAndSet(true, false)) {
			log.info(ARTHAS_ALREADY_DESTROY);
			return;
		}
		log.info(DESTROY_ARTHAS);
		DefaultListableBeanFactory defaultListableBeanFactory = getDefaultListableBeanFactory();
		if (defaultListableBeanFactory.containsBean(ARTHAS_AGENT)) {
			defaultListableBeanFactory.destroySingleton(ARTHAS_AGENT);
		}
	}

	@NotNull
	private DefaultListableBeanFactory getDefaultListableBeanFactory() {
		return (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
	}
}
