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

import com.alibaba.arthas.spring.ArthasConfiguration;
import com.alibaba.arthas.spring.ArthasProperties;
import com.alibaba.arthas.spring.StringUtils;
import com.taobao.arthas.agent.attach.ArthasAgent;
import com.taobao.arthas.agent.attach.AttachArthasClassloader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Role;
import org.springframework.core.env.Environment;
import org.ylzl.eden.arthas.spring.boot.env.SpringArthasProperties;
import org.ylzl.eden.spring.framework.bootstrap.constant.SpringProperties;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class ArthasAutoConfiguration {

	private static final String ARTHAS_CONFIG_MAP = "arthasConfigMap";

	private static final String APP_NAME = "appName";

	private static final String ARTHAS_PROPERTIES_PREFIX = "arthas.";

	private static final String ARTHAS_AGENT_START_SUCCESS = "Arthas agent start success";

	private static final int TEMP_DIR_ATTEMPTS = 10000;

	private static final Pattern ARTHAS_DIR_PATTERN = Pattern.compile("^arthas-(\\d{13})-.*$");

	private static final String ARTHAS_BIN_ZIP = "arthas-bin.zip";

	private static final String ARTHAS_CORE_JAR = "arthas-core.jar";

	private static final String ARTHAS_BOOTSTRAP = "com.taobao.arthas.core.server.ArthasBootstrap";

	private static final String GET_INSTANCE = "getInstance";

	private static final String DESTROY = "destroy";

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
		return init(arthasConfigMap, environment, arthasProperties);
	}

	public static ArthasAgent init(Map<String, String> arthasConfigMap, Environment environment,
											  ArthasProperties arthasProperties) {
		ArthasProperties.updateArthasConfigMapDefaultValue(arthasConfigMap);

		String appName = environment.getProperty(SpringProperties.SPRING_APPLICATION_NAME);
		if (arthasConfigMap.get(APP_NAME) == null && appName != null) {
			arthasConfigMap.put(APP_NAME, appName);
		}

		Map<String, String> mapWithPrefix = new HashMap<>(arthasConfigMap.size());
		for (Map.Entry<String, String> entry : arthasConfigMap.entrySet()) {
			mapWithPrefix.put(ARTHAS_PROPERTIES_PREFIX + entry.getKey(), entry.getValue());
		}

		final ArthasAgent arthasAgent = new ArthasAgent(mapWithPrefix, arthasProperties.getHome(),
			arthasProperties.isSlientInit(), null);

		arthasAgent.init();
		log.info(ARTHAS_AGENT_START_SUCCESS);
		return arthasAgent;
	}

	public static void destroy(Map<String, String> arthasConfigMap,
							   ArthasProperties arthasProperties) {
		String arthasHome = arthasProperties.getHome();
		try {
			Instrumentation instrumentation = ByteBuddyAgent.install();

			if (arthasHome == null || arthasHome.trim().isEmpty()) {
				ClassLoader classLoader = ArthasAgent.class.getClassLoader();
				URL coreJarUrl = classLoader.getResource(ARTHAS_BIN_ZIP);
				if (coreJarUrl != null) {
					File tempArthasDir = findTempDir();
					arthasHome = tempArthasDir.getAbsolutePath();
				} else {
					throw new IllegalArgumentException("can not getResources arthas-bin.zip from classloader: "
						+ classLoader);
				}
			}
			File arthasCoreJarFile = new File(arthasHome, ARTHAS_CORE_JAR);
			if (!arthasCoreJarFile.exists()) {
				throw new IllegalArgumentException("can not find arthas-core.jar under arthasHome: {}" + arthasHome);
			}
			AttachArthasClassloader arthasClassLoader = new AttachArthasClassloader(
				new URL[] { arthasCoreJarFile.toURI().toURL() });
			Class<?> bootstrapClass = arthasClassLoader.loadClass(ARTHAS_BOOTSTRAP);

			Object bootstrap = bootstrapClass.getMethod(GET_INSTANCE, Instrumentation.class, Map.class)
				.invoke(null, instrumentation, arthasConfigMap);
			bootstrapClass.getMethod(DESTROY).invoke(bootstrap);
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
	}

	private static List<File> listArthasDirs(File baseDir) {
		return Stream.of(baseDir.listFiles((dir, name) -> ARTHAS_DIR_PATTERN.matcher(name).matches()))
			.collect(Collectors.toList());
	}

	private static Date extractTimestamp(File dir) {
		Matcher matcher = ARTHAS_DIR_PATTERN.matcher(dir.getName());
		if (matcher.matches()) {
			String timestampStr = matcher.group(1);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			try {
				return sdf.parse(timestampStr);
			} catch (ParseException e) {
				throw new IllegalArgumentException("Invalid timestamp format in directory name: " + dir.getName(), e);
			}
		}
		return null;
	}

	private static File findLatestArthasDir(File baseDir) {
		List<File> dirs = listArthasDirs(baseDir);
		if (!dirs.isEmpty()) {
			return dirs.stream()
				.max(Comparator.comparing(ArthasAutoConfiguration::extractTimestamp))
				.orElse(null);
		}
		return null;
	}

	public static File findTempDir() {
		File baseDir = new File(System.getProperty("java.io.tmpdir"));
		String baseName = "arthas-" + System.currentTimeMillis() + "-";

		// 尝试查找时间戳最大的以 "arthas-" 开头的目录
		File latestDir = findLatestArthasDir(baseDir);
		if (latestDir != null) {
			return latestDir;
		}

		throw new IllegalStateException("Failed to find directory within " + TEMP_DIR_ATTEMPTS
			+ " attempts (tried " + baseName + "0 to " + baseName + (TEMP_DIR_ATTEMPTS - 1) + ')');
	}
}
