package org.ylzl.eden.spring.boot.xxljob.autoconfigure;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import com.xxl.job.core.util.IpUtil;
import com.xxl.job.core.util.NetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.ylzl.eden.spring.boot.xxljob.env.XxlJobProperties;
import org.ylzl.eden.spring.framework.bootstrap.constant.SpringPropertiesConstants;

import java.io.File;

/**
 * XXLJob 自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(name = XxlJobProperties.PREFIX + ".enable", havingValue = "true")
@EnableConfigurationProperties(XxlJobProperties.class)
@Slf4j
@Configuration
public class XxlJobAutoCofiguration {

	private static final String AUTOWIRED_XXL_JOB_SPRING_EXECUTOR = "Autowired XxlJobSpringExecutor";

	private static final int MAX_PORT = 65535;

	private final Environment environment;

	private final XxlJobProperties xxlJobProperties;

	public XxlJobAutoCofiguration(Environment environment, XxlJobProperties xxlJobProperties) {
		this.environment = environment;
		this.xxlJobProperties = xxlJobProperties;
	}

	@Bean
	public XxlJobSpringExecutor xxlJobSpringExecutor() {
		log.info(AUTOWIRED_XXL_JOB_SPRING_EXECUTOR);
		XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
		xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdmin().getAddresses());
		if (StringUtils.isNotBlank(xxlJobProperties.getAdmin().getAccessToken())) {
			xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getAdmin().getAccessToken());
		}

		String appName = resolveAppName(xxlJobProperties.getExecutor().getAppName());
		xxlJobSpringExecutor.setAppname(appName);
		xxlJobSpringExecutor.setIp(resolveIp(xxlJobProperties.getExecutor().getIp()));
		xxlJobSpringExecutor.setPort(resolvePort(xxlJobProperties.getExecutor().getPort()));
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

	private int resolvePort(Integer port) {
		if (port != null) {
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
		return StringUtils.join(userHome, File.separator, "logs", File.separator,
			"xxl-job", File.separator, appName);
	}
}
