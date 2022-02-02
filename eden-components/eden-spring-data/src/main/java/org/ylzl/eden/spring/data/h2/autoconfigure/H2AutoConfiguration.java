package org.ylzl.eden.spring.data.h2.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.ylzl.eden.commons.lang.ObjectUtils;
import org.ylzl.eden.spring.data.core.constant.SpringDataConstants;
import org.ylzl.eden.spring.data.h2.env.H2Properties;
import org.ylzl.eden.spring.data.h2.util.H2ConfigurationHelper;

import java.sql.SQLException;
import java.util.Objects;

/**
 * H2 自动装配
 *
 * @author gyl
 * @since 2.4.x
 */
@ConditionalOnExpression(H2AutoConfiguration.EXP_H2_ENABLED)
@EnableConfigurationProperties(H2Properties.class)
@Slf4j
@Configuration
public class H2AutoConfiguration {

	public static final String EXP_H2_ENABLED = "${" + SpringDataConstants.PROP_PREFIX + ".h2.enabled:false}";

	private final Environment env;

	private final H2Properties properties;

	public H2AutoConfiguration(Environment env, H2Properties properties) {
		this.env = env;
		this.properties = properties;
	}

	@Bean(initMethod = "start", destroyMethod = "stop")
	public Object h2TCPServer() throws SQLException {
		int port = getValidPortForH2();
		log.info("H2 database is available on port {}", port);
		return H2ConfigurationHelper.createServer(port);
	}

	private int getValidPortForH2() {
		if (ObjectUtils.isNotNull(properties.getPort())) {
			return properties.getPort();
		}
		int port = Integer.parseInt(Objects.requireNonNull(env.getProperty("server.port")));
		if (port < 10000) {
			return 10000 + port;
		}
		if (port < 63536) {
			return port + 2000;
		}
		return port - 2000;
	}
}
