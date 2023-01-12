package org.ylzl.eden.nacos.config.spring.cloud.autoconfigure;

import com.alibaba.cloud.nacos.NacosConfigBootstrapConfiguration;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.nacos.config.spring.cloud.env.Log4j2NacosProperties;
import org.ylzl.eden.nacos.config.spring.cloud.util.LocalConfigInfoProcessorExporter;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

import java.io.File;
import java.net.URI;

/**
 * Log4j2 基于 Nacos 刷新配置文件自动装配
 * <p> Nacos 缓存配置路径规则：${user.home}/nacos/config/fixed-host_port-namespace_tenant/snapshot-tenant/namespace/group
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = Log4j2NacosProperties.PREFIX,
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE,
	matchIfMissing = true
)
@AutoConfigureAfter(NacosConfigBootstrapConfiguration.class)
@EnableConfigurationProperties(Log4j2NacosProperties.class)
@ConditionalOnClass(LogManager.class)
@RequiredArgsConstructor
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class Log4j2NacosAutoConfiguration implements InitializingBean {

	private static final String RPC_CLIENT = "config_rpc_client";

	private static final String FIXED = "fixed";

	private final NacosConfigProperties nacosConfigProperties;

	private final Log4j2NacosProperties log4j2ConfigProperties;

	@Override
	public void afterPropertiesSet() {
		File configFile = this.getFile(RPC_CLIENT);
		if (!configFile.exists()) {
			configFile = this.getFile(getServerName());
		}
		if (!configFile.exists()) {
			log.warn("Loading log4j2 config file from nacos config cache failed");
			return;
		}
		URI uri = configFile.toURI();
		log.info("Loading log4j2 config file from nacos config cache: {}", uri);
		LoggerContext loggerContext = (LoggerContext) LogManager.getContext();
		loggerContext.setConfigLocation(uri);
		loggerContext.reconfigure();
		log.info("Loading log4j2 config file finished.");
	}

	@NacosConfigListener(
		groupId = "${log4j2.nacos.group}",
		dataId = "${log4j2.nacos.data-id}",
		timeout = 3000
	)
	public void onChange(String xml) {
		log.debug(xml);
	}

	private File getFile(String name) {
		File file = getFailoverFile(name);
		if (!file.exists()) {
			file = getSnapshotFile(name);
		}
		return file;
	}

	private File getSnapshotFile(String name) {
		return LocalConfigInfoProcessorExporter.getSnapshotFile(name,
			log4j2ConfigProperties.getNacos().getDataId(),
			log4j2ConfigProperties.getNacos().getGroup(), getNamespace());
	}

	private File getFailoverFile(String name) {
		return LocalConfigInfoProcessorExporter.getFailoverFile(name,
			log4j2ConfigProperties.getNacos().getDataId(),
			log4j2ConfigProperties.getNacos().getGroup(), getNamespace());
	}

	private String getServerName() {
		return StringUtils.join(FIXED, "-", getServerAddr());
	}

	private String getServerAddr() {
		return nacosConfigProperties.getServerAddr()
			.replaceAll("http(s)?://", "")
			.replaceAll(":", "_");
	}

	private String getNamespace() {
		return nacosConfigProperties.getNamespace();
	}
}
