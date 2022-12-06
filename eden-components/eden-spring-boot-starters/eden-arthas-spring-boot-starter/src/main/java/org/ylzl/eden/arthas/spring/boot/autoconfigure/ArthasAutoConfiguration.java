package org.ylzl.eden.arthas.spring.boot.autoconfigure;

import com.alibaba.arthas.spring.ArthasConfiguration;
import com.alibaba.arthas.spring.ArthasProperties;
import com.alibaba.arthas.spring.StringUtils;
import com.taobao.arthas.agent.attach.ArthasAgent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.ylzl.eden.arthas.spring.boot.env.SpringArthasProperties;
import org.ylzl.eden.arthas.spring.boot.listener.ArthasEnvironmentChangeListener;
import org.ylzl.eden.spring.framework.bootstrap.constant.SpringProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Arthas 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	name = {"spring.arthas.enabled"},
	matchIfMissing = true
)
@AutoConfigureAfter(ArthasConfiguration.class)
@EnableConfigurationProperties(SpringArthasProperties.class)
@RequiredArgsConstructor
@Slf4j
@Configuration
public class ArthasAutoConfiguration {

	private static final String ARTHAS_CONFIG_MAP = "arthasConfigMap";

	private static final String APP_NAME = "appName";

	private static final String ARTHAS_PROPERTIES_PREFIX = "arthas.";

	private static final String ARTHAS_AGENT_START_SUCCESS = "Arthas agent start success";

	private final ApplicationContext applicationContext;

	private final Environment environment;

	private final ArthasProperties arthasProperties;

	@Bean
	public ArthasEnvironmentChangeListener arthasEnvironmentChangeListener(
		@Autowired @Qualifier(ARTHAS_CONFIG_MAP) Map<String, String> arthasConfigMap) {
		return new ArthasEnvironmentChangeListener(applicationContext,
			environment, arthasProperties, arthasConfigMap);
	}

	@Primary
	@Bean
	public ArthasAgent arthasAgent(@Autowired @Qualifier(ARTHAS_CONFIG_MAP) Map<String, String> arthasConfigMap) {
		arthasConfigMap = StringUtils.removeDashKey(arthasConfigMap);
		return initArthasAgent(arthasConfigMap, environment, arthasProperties);
	}

	public static ArthasAgent initArthasAgent(Map<String, String> arthasConfigMap, Environment environment,
											  ArthasProperties arthasProperties) {
		ArthasProperties.updateArthasConfigMapDefaultValue(arthasConfigMap);

		String appName = environment.getProperty(SpringProperties.SPRING_APPLICATION_NAME);
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
}
