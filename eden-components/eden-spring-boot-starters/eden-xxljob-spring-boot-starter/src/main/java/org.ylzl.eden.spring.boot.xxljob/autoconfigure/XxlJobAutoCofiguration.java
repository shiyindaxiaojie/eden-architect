package com.puyiwm.spring.boot.xxljob.autoconfigure;

import com.puyiwm.spring.boot.xxljob.env.XxlJobProperties;
import com.xxl.job.core.executor.XxlJobExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * XXLJob 自动配置
 *
 * @author gyl
 * @since 2.4.x
 */
@ConditionalOnExpression("${xxl.job.enabled:false}")
@EnableConfigurationProperties(XxlJobProperties.class)
@Slf4j
@Configuration
public class XxlJobAutoCofiguration {

	private final XxlJobProperties xxlJobProperties;

	public XxlJobAutoCofiguration(XxlJobProperties xxlJobProperties) {
		this.xxlJobProperties = xxlJobProperties;
	}

	@Bean(initMethod = "start", destroyMethod = "destroy")
	public XxlJobExecutor xxlJobExecutor() {
		log.info("Autowired XxlJobExecutor");
		XxlJobExecutor xxlJobExecutor = new XxlJobExecutor();
		xxlJobExecutor.setAccessToken(xxlJobProperties.getAccessToken());
		xxlJobExecutor.setAdminAddresses(xxlJobProperties.getAdmin().getAddresses());
		xxlJobExecutor.setAppName(xxlJobProperties.getExecutor().getAppName());
		xxlJobExecutor.setIp(xxlJobProperties.getExecutor().getIp());
		xxlJobExecutor.setPort(xxlJobProperties.getExecutor().getPort());
		xxlJobExecutor.setLogPath(xxlJobProperties.getExecutor().getLogPath());
		xxlJobExecutor.setLogRetentionDays(xxlJobProperties.getExecutor().getLogRetentionDays());
		return xxlJobExecutor;
	}
}
