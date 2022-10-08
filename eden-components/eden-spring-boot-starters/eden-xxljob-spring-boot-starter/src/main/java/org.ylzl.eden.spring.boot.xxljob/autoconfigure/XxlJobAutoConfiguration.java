package org.ylzl.eden.spring.boot.xxljob.autoconfigure;

import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import com.xxl.job.core.util.IpUtil;
import com.xxl.job.core.util.NetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import org.ylzl.eden.spring.boot.xxljob.env.XxlJobProperties;
import org.ylzl.eden.spring.framework.bootstrap.constant.SpringPropertiesConstants;
import org.ylzl.eden.spring.integration.xxljob.core.AutoRegisterXxlJobSpringExecutor;
import org.ylzl.eden.spring.integration.xxljob.core.XxlJobAdminTemplate;

import java.io.File;

/**
 * XXLJob 自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnClass(XxlJobExecutor.class)
@ConditionalOnProperty(name = XxlJobProperties.ENABLED, havingValue = "true")
@EnableConfigurationProperties(XxlJobProperties.class)
@Slf4j
@Configuration
public class XxlJobAutoConfiguration {

	private static final String AUTOWIRED_XXL_JOB_SPRING_EXECUTOR = "Autowired XxlJobSpringExecutor";

	public static final String DEFAULT_JOB_HANDLER_LOGS_PATH = "/logs/xxl-job/job-handler";

	private static final int MAX_PORT = 65535;

	private final Environment environment;

	private final XxlJobProperties xxlJobProperties;

	public XxlJobAutoConfiguration(Environment environment, XxlJobProperties xxlJobProperties) {
		this.environment = environment;
		this.xxlJobProperties = xxlJobProperties;
	}

	@Bean
	public XxlJobAdminTemplate xxlJobAdminTemplate(RestTemplate restTemplate) {
		return new XxlJobAdminTemplate(restTemplate, xxlJobProperties.getAccessToken(),
			xxlJobProperties.getAdmin().getAddresses());
	}

	@ConditionalOnMissingBean
	@Bean(initMethod = "start", destroyMethod = "destroy")
	public XxlJobSpringExecutor xxlJobSpringExecutor(XxlJobAdminTemplate xxlJobAdminTemplate) {
		log.info(AUTOWIRED_XXL_JOB_SPRING_EXECUTOR);
		XxlJobSpringExecutor xxlJobSpringExecutor = new AutoRegisterXxlJobSpringExecutor(xxlJobAdminTemplate);
		xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdmin().getAddresses());

		if (StringUtils.isNotBlank(xxlJobProperties.getAccessToken())) {
			xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getAccessToken());
		}

		String appName = resolveAppName(xxlJobProperties.getExecutor().getAppName());
		xxlJobSpringExecutor.setAppname(appName);
		if (StringUtils.isNotBlank(xxlJobProperties.getExecutor().getAddress())) {
			xxlJobSpringExecutor.setAddress(xxlJobProperties.getExecutor().getAddress());
		} else {
			xxlJobSpringExecutor.setIp(resolveIp(xxlJobProperties.getExecutor().getIp()));
			xxlJobSpringExecutor.setPort(resolvePort(xxlJobProperties.getExecutor().getPort()));
		}
		xxlJobSpringExecutor.setLogPath(resolveLogPath(xxlJobProperties.getExecutor().getLogPath(), appName));
		xxlJobSpringExecutor.setLogRetentionDays(xxlJobProperties.getExecutor().getLogRetentionDays());
		return xxlJobSpringExecutor;
	}

	private String resolveAppName(String appName) {
		if (StringUtils.isNotBlank(appName)) {
			return appName;
		}

		return environment.getProperty(SpringPropertiesConstants.SPRING_APPLICATION_NAME);
	}

	private String resolveIp(String ip) {
		if (StringUtils.isNotBlank(ip)) {
			return ip;
		}

		ip = IpUtil.getIp();
		log.warn("XxlJob auto get address: {}", ip);
		return ip;
	}

	private int resolvePort(int port) {
		if (port > 0) {
			return port;
		}
		port = NetUtil.findAvailablePort(MAX_PORT);
		log.warn("XxlJob auto get port: {}", port);
		return port;
	}

	private String resolveLogPath(String logPath, String appName) {
		if (StringUtils.isNotBlank(logPath)) {
			return logPath;
		}
		String userHome = environment.getProperty("user.home");
		return StringUtils.join(userHome, DEFAULT_JOB_HANDLER_LOGS_PATH, File.separator, appName);
	}
}
