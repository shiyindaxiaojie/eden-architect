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
import org.ylzl.eden.spring.boot.arthas.env.SpringArthasProperties;
import org.ylzl.eden.spring.framework.bootstrap.constant.SpringPropertiesConstants;

import javax.annotation.PostConstruct;
import java.util.HashMap;
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

	public static final AtomicBoolean REGISTER_STATE = new AtomicBoolean(false);

	public static final String APP_NAME = "appName";
	public static final String ARTHAS_AGENT = "arthasAgent";

	public static final String ARTHAS_PROPERTIES_PREFIX = "arthas.";
	public static final String ARTHAS_AGENT_START_SUCCESS = "Arthas agent start success.";

	public static final String REGISTER_ARTHAS = "Register arthas";

	public static final String ARTHAS_ALREADY_REGISTERED = "Arthas already registered";

	public static final String DESTORY_ARTHAS = "Destory arthas";

	public static final String ARTHAS_ALREADY_DESTORY = "Arthas already destory";

	private final Environment environment;

	private final ApplicationContext applicationContext;

	private final ArthasProperties arthasProperties;

	private final SpringArthasProperties springArthasProperties;

	private final Map<String, String> arthasConfigMap;


	@Override
	public void onApplicationEvent(EnvironmentChangeEvent event) {
		Set<String> keys = event.getKeys();
		for (String key : keys) {
			if (SpringArthasProperties.ENABLED.equals(key)) {
				if (Boolean.parseBoolean(environment.getProperty(key))) {
					registerBean();
				} else {
					destoryBean();
				}
				break;
			}
		}
	}

	@PostConstruct
	public void init() {
		if (springArthasProperties.isEnable()) {
			registerBean();
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
			defaultListableBeanFactory.registerSingleton(ARTHAS_AGENT, initArthasAgent());
		}
	}

	private void destoryBean() {
		if (!REGISTER_STATE.compareAndSet(true, false)) {
			log.info(ARTHAS_ALREADY_DESTORY);
			return;
		}
		log.info(DESTORY_ARTHAS);
		DefaultListableBeanFactory defaultListableBeanFactory = getDefaultListableBeanFactory();
		if (defaultListableBeanFactory.containsBean(ARTHAS_AGENT)) {
			defaultListableBeanFactory.destroySingleton(ARTHAS_AGENT);
		}
	}

	private ArthasAgent initArthasAgent() {
		ArthasProperties.updateArthasConfigMapDefaultValue(arthasConfigMap);

		String appName = environment.getProperty(SpringPropertiesConstants.NAME_PATTERN);
		if (arthasConfigMap.get(APP_NAME) == null && appName != null) {
			arthasConfigMap.put(APP_NAME, appName);
		}

		Map<String, String> mapWithPrefix = new HashMap<String, String>(arthasConfigMap.size());
		for (Map.Entry<String, String> entry : arthasConfigMap.entrySet()) {
			mapWithPrefix.put(ARTHAS_PROPERTIES_PREFIX + entry.getKey(), entry.getValue());
		}

		final ArthasAgent arthasAgent = new ArthasAgent(mapWithPrefix, arthasProperties.getHome(),
			arthasProperties.isSlientInit(), null);

		arthasAgent.init();
		log.info(ARTHAS_AGENT_START_SUCCESS);
		return arthasAgent;
	}

	@NotNull
	private DefaultListableBeanFactory getDefaultListableBeanFactory() {
		return (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
	}
}
